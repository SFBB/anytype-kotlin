package com.anytypeio.anytype.app

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import com.anytypeio.anytype.R
import com.anytypeio.anytype.core_models.Id
import com.anytypeio.anytype.core_models.Notification
import com.anytypeio.anytype.core_models.NotificationPayload
import com.anytypeio.anytype.core_models.Relations
import com.anytypeio.anytype.domain.notifications.SystemNotificationService
import com.anytypeio.anytype.ui.main.MainActivity
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

class AnytypeNotificationService @Inject constructor(
    private val context: Context
) : SystemNotificationService {

    private val mutex = Mutex()
    private var pending: Notification? = null

    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    override val areNotificationsEnabled: Boolean
        get() = notificationManager.areNotificationsEnabled()

    override suspend fun setPendingNotification(notification: Notification) {
        mutex.withLock {
            pending = notification
        }
    }

    override suspend fun clearPendingNotification() {
        mutex.withLock {
            pending = null
        }
    }

    override suspend fun notifyIfPending() {
        mutex.withLock {
            val notification = pending
            if (notification != null) {
                notify(notification).also {
                    pending = null
                }
            }
        }
    }

    override fun notify(
        notification: Notification
    ) {
        when(val payload = notification.payload) {
            is NotificationPayload.ParticipantPermissionsChange -> {
                val placeholder = context.resources.getString(R.string.untitled)
                val title = context.resources.getString(
                    R.string.multiplayer_notification_member_permissions_changed
                )
                val body = if (payload.permissions.isOwnerOrEditor())
                    context.resources.getString(
                        R.string.multiplayer_notification_member_permission_change_edit,
                        payload.spaceName.ifEmpty { placeholder }
                    )
                else
                    context.resources.getString(
                        R.string.multiplayer_notification_member_permission_change_read,
                        payload.spaceName.ifEmpty { placeholder }
                    )

                showBasicNotification(
                    tag = notification.id,
                    title = title,
                    body = body,
                    actions = emptyList()
                )
            }
            is NotificationPayload.ParticipantRemove -> {
                val placeholder = context.resources.getString(R.string.untitled)
                val body = context.resources.getString(
                    R.string.multiplayer_notification_member_removed_from_space,
                    payload.spaceName.ifEmpty { placeholder }
                )
                val title = context.resources.getString(
                    R.string.multiplayer_notification_member_removed_from_space_title,
                    payload.spaceName.ifEmpty { placeholder }
                )
                showBasicNotification(
                    tag = notification.id,
                    body = body,
                    title = title
                )
            }
            is NotificationPayload.ParticipantRequestApproved -> {
                Timber.d("Processing participant request approved notification : ${notification}")
                val placeholder = context.resources.getString(R.string.untitled)
                val title = context.resources.getString(
                    R.string.multiplayer_notification_member_request_approved
                )
                val actionTitle = context.resources.getString(
                    R.string.multiplayer_notification_go_to_space
                )
                val permissions = payload.permissions
                val body = when {
                    permissions == null -> {
                        context.resources.getString(
                            R.string.multiplayer_notification_member_request_approved_unknown_rights,
                            payload.spaceName.ifEmpty { placeholder }
                        )
                    }
                    permissions.isOwnerOrEditor() -> {
                        context.resources.getString(
                            R.string.multiplayer_notification_member_request_approved_with_edit_rights,
                            payload.spaceName.ifEmpty { placeholder }
                        )
                    }
                    else -> {
                        context.resources.getString(
                            R.string.multiplayer_notification_member_request_approved_with_read_only_rights,
                            payload.spaceName.ifEmpty { placeholder }
                        )
                    }
                }
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra(Relations.SPACE_ID, payload.spaceId.id)
                    putExtra(NOTIFICATION_TYPE, REQUEST_APPROVED_TYPE)
                    putExtra(NOTIFICATION_ID_KEY, notification.id)
                    setType(REQUEST_APPROVED_TYPE.toString())
                    setAction(NOTIFICATION_INTENT_ACTION)
                }

                val activity = PendingIntent.getActivity(
                    context,
                    notification.hashCode(),
                    intent,
                    getDefaultFlags()
                )
                showBasicNotification(
                    tag = notification.id,
                    title = title,
                    body = body,
                    actions = buildList {
                        add(NotificationCompat.Action(0, actionTitle, activity))
                    }
                )
            }
            is NotificationPayload.ParticipantWithoutApprovalRequestApproved -> {
                Timber.d("Processing auto-approved join notification : ${notification}")
                val placeholder = context.resources.getString(R.string.untitled)
                val title = context.resources.getString(
                    R.string.multiplayer_notification_member_request_approved
                )
                val actionTitle = context.resources.getString(
                    R.string.multiplayer_notification_go_to_space
                )
                val body = context.resources.getString(
                    R.string.multiplayer_notification_member_join_without_approval,
                    payload.spaceName.ifEmpty { placeholder }
                )
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra(Relations.SPACE_ID, payload.spaceId.id)
                    putExtra(NOTIFICATION_TYPE, REQUEST_APPROVED_TYPE)
                    putExtra(NOTIFICATION_ID_KEY, notification.id)
                    setType(REQUEST_APPROVED_TYPE.toString())
                    setAction(NOTIFICATION_INTENT_ACTION)
                }

                val activity = PendingIntent.getActivity(
                    context,
                    notification.hashCode(),
                    intent,
                    getDefaultFlags()
                )
                showBasicNotification(
                    tag = notification.id,
                    title = title,
                    body = body,
                    actions = buildList {
                        add(Action(0, actionTitle, activity))
                    }
                )
            }
            is NotificationPayload.ParticipantRequestDecline -> {
                val placeholder = context.resources.getString(R.string.untitled)
                val title = context.resources.getString(
                    R.string.multiplayer_notification_request_declined
                )
                val body = context.resources.getString(
                    R.string.multiplayer_notification_member_join_request_declined,
                    payload.spaceName.ifEmpty { placeholder }
                )
                showBasicNotification(
                    tag = notification.id,
                    title = title,
                    body = body
                )
            }
            is NotificationPayload.RequestToJoin -> {
                val placeholder = context.resources.getString(R.string.untitled)
                val title = context.resources.getString(R.string.multiplayer_notification_new_join_request)
                val actionTitle = context.resources.getString(R.string.multiplayer_view_request)
                val body = context.resources.getString(
                    R.string.multiplayer_notification_member_user_sends_join_request,
                    payload.identityName.ifEmpty { placeholder },
                    payload.spaceName.ifEmpty { placeholder },
                )

                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra(Relations.SPACE_ID, payload.spaceId.id)
                    putExtra(NOTIFICATION_TYPE, REQUEST_TO_JOIN_TYPE)
                    putExtra(NOTIFICATION_ID_KEY, notification.id)
                    putExtra(Relations.IDENTITY, payload.identity)
                    setType(REQUEST_TO_JOIN_TYPE.toString())
                    setAction(NOTIFICATION_INTENT_ACTION)
                }

                val activity = PendingIntent.getActivity(
                    context,
                    notification.hashCode(),
                    intent,
                    getDefaultFlags()
                )

                showBasicNotification(
                    tag = notification.id,
                    title = title,
                    body = body,
                    actions = buildList {
                        add(NotificationCompat.Action(0, actionTitle, activity))
                    }
                )
            }
            is NotificationPayload.RequestToLeave -> {
                val placeholder = context.resources.getString(R.string.untitled)
                val title = context.resources.getString(R.string.multiplayer_leave_request)
                val actionTitle = context.resources.getString(R.string.multiplayer_view_request)
                val body = context.resources.getString(
                    R.string.multiplayer_notification_member_user_sends_leave_request,
                    payload.identityName.ifEmpty { placeholder },
                    payload.spaceName.ifEmpty { placeholder },
                )
                val intent = Intent(context, MainActivity::class.java).apply {
                    putExtra(Relations.SPACE_ID, payload.spaceId.id)
                    putExtra(NOTIFICATION_TYPE, REQUEST_TO_LEAVE_TYPE)
                    putExtra(NOTIFICATION_ID_KEY, notification.id)
                    putExtra(Relations.IDENTITY, payload.identity)
                    setType(REQUEST_TO_LEAVE_TYPE.toString())
                    setAction(NOTIFICATION_INTENT_ACTION)
                }
                val activity = PendingIntent.getActivity(
                    context,
                    notification.hashCode(),
                    intent,
                    getDefaultFlags()
                )
                showBasicNotification(
                    tag = notification.id,
                    title = title,
                    body = body,
                    actions = buildList {
                        add(NotificationCompat.Action(0, actionTitle, activity))
                    }
                )
            }
            else -> {
                Timber.w("Ignoring notification")
            }
        }
    }

    override fun cancel(id: String) {
        Timber.d("Cancelling notification with id: $id")
        notificationManager.cancel(id, 0)
    }

    private fun showBasicNotification(
        tag: Id,
        title: String? = null,
        body: String,
        actions: List<NotificationCompat.Action> = emptyList()
    ) {
        Timber.d("Attempt to show notification")
        val builder = NotificationCompat.Builder(
            context,
            AndroidApplication.NOTIFICATION_CHANNEL_ID
        )
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_app_notification)
        val notification = with(builder) {
            setSmallIcon(R.drawable.ic_app_notification)
            setLargeIcon(icon)
            if (!title.isNullOrEmpty()) {
                setContentTitle(title)
            }
            setPriority(NotificationManager.IMPORTANCE_HIGH)
            setAutoCancel(true)
            actions.forEach { addAction(it) }
            setStyle(NotificationCompat.BigTextStyle().bigText(body))
            build()
        }

        notificationManager.notify(tag, 0, notification)
    }

    private fun getDefaultFlags(): Int {
        return PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    }

    companion object {
        const val NOTIFICATION_ID_KEY = "notification"
        const val NOTIFICATION_TYPE = "type"
        const val REQUEST_TO_JOIN_TYPE = 0
        const val REQUEST_TO_LEAVE_TYPE = 1
        const val REQUEST_APPROVED_TYPE = 2
        const val REQUEST_DECLINED_TYPE = 3
        const val MEMBER_REMOVED_TYPE = 4
        const val PERMISSIONS_CHANGED_TYPE = 5

        const val NOTIFICATION_INTENT_ACTION = "io.anytype.app.notification-action"

        val defaultDuration = 1L.toDuration(DurationUnit.HOURS).inWholeMilliseconds
    }
}