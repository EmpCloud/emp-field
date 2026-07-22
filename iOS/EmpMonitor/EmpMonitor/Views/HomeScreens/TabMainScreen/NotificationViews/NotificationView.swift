//
//  NotificationView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 09/09/24.
//

import SwiftUI

struct NotificationView: View {

    @Environment(\.dismiss) var dismiss

    @ObservedObject var notificationViewModel: NotificationViewModel

    private var unreadCount: Int {
        notificationViewModel.notificationList.filter { notificationViewModel.isUnread($0) }.count
    }

    var body: some View {
        ZStack {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()

            RoundedRectangle(cornerRadius: AppRadius.large)
                .fill(Color.rectangleBG)
                .padding(.top)
                .ignoresSafeArea(edges: .bottom)
                .shadow(color: .black.opacity(0.25), radius: 12)
                .overlay(alignment: .top) {
                    VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
                        notificationHeader
                        notificationContent
                    }
                    .padding(.top, AppSpacing.lg)
                }
        }
        .onAppear {
            Task {
                try await notificationViewModel.getNotificationList()
            }
        }
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                BackButtonView()
                    .onTapGesture {
                        dismiss()
                    }
            }
            ToolbarItem(placement: .principal) {
                Text("Notifications")
                    .font(AppFont.primary(size: AppFont.Size.navigationTitle))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.white)
            }
        }
    }

    private var notificationHeader: some View {
        HStack(alignment: .center, spacing: AppSpacing.stackSpacingDefault) {
            VStack(alignment: .leading, spacing: AppSpacing.xs) {
                Text("\(notificationViewModel.notificationList.count) notifications")
                    .font(AppFont.primary(size: AppFont.Size.callout))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.taskClientName)

                Text(unreadCount > 0 ? "\(unreadCount) new updates" : "Everything is read")
                    .font(AppFont.primary(size: AppFont.Size.footnote))
                    .fontWeight(AppFont.Weight.medium)
                    .foregroundStyle(Color.subText)
            }

            Spacer(minLength: AppSpacing.sm)

            if notificationViewModel.hasUnreadNotifications {
                Button {
                    notificationViewModel.markAllAsRead()
                } label: {
                    HStack(spacing: AppSpacing.compactIconTextSpacing) {
                        Image(systemName: AppIcons.checkmark)
                            .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))

                        Text("Mark all read")
                            .font(AppFont.primary(size: AppFont.Size.caption))
                            .fontWeight(AppFont.Weight.semibold)
                    }
                    .foregroundStyle(Color.primaryButton1)
                    .padding(.horizontal, AppSpacing.sm)
                    .frame(minHeight: AppLayout.minimumTouchTarget)
                    .contentShape(Rectangle())
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Mark all notifications as read")
            }
        }
        .padding(.horizontal, AppSpacing.screenHorizontalPadding)
        .frame(minHeight: AppLayout.minimumTouchTarget)
    }

    @ViewBuilder
    private var notificationContent: some View {
        if notificationViewModel.isLoading {
            loadingState
        } else if let error = notificationViewModel.error {
            errorState(error)
        } else if notificationViewModel.notificationList.isEmpty {
            emptyState
        } else {
            notificationList
        }
    }

    private var loadingState: some View {
        VStack(spacing: AppSpacing.stackSpacingDefault) {
            Spacer()
            ProgressView()
                .progressViewStyle(.circular)
                .scaleEffect(1.2)
            Text("Loading notifications...")
                .font(AppFont.primary(size: AppFont.Size.callout))
                .foregroundStyle(Color.secondary)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private func errorState(_ error: Error) -> some View {
        VStack(spacing: AppSpacing.stackSpacingDefault) {
            Spacer()
            Image(systemName: AppIcons.offline)
                .font(AppFont.primary(size: AppFont.Size.iconLarge))
                .foregroundStyle(Color.secondary)

            Text("Failed to load notifications")
                .font(AppFont.primary(size: AppFont.Size.body))
                .fontWeight(AppFont.Weight.semibold)
                .foregroundStyle(Color.primary)

            Text(error.localizedDescription)
                .font(AppFont.primary(size: AppFont.Size.caption))
                .foregroundStyle(Color.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, AppSpacing.lg)

            Button {
                Task { try? await notificationViewModel.getNotificationList() }
            } label: {
                Text("Retry")
                    .font(AppFont.primary(size: AppFont.Size.callout))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(.white)
                    .padding(.horizontal, AppSpacing.lg)
                    .frame(minHeight: AppLayout.minimumTouchTarget)
                    .background(Color.primaryButton1)
                    .clipShape(Capsule())
            }
            .buttonStyle(.plain)

            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private var emptyState: some View {
        VStack(spacing: AppSpacing.stackSpacingDefault) {
            Spacer()
            Image(systemName: AppIcons.notificationOff)
                .font(AppFont.primary(size: AppFont.Size.iconLarge))
                .foregroundStyle(Color.secondary)

            Text("No notifications")
                .font(AppFont.primary(size: AppFont.Size.body))
                .fontWeight(AppFont.Weight.semibold)
                .foregroundStyle(Color.primary)

            Text("You're all caught up.")
                .font(AppFont.primary(size: AppFont.Size.caption))
                .foregroundStyle(Color.secondary)

            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }

    private var notificationList: some View {
        ScrollView {
            LazyVStack(spacing: AppSpacing.stackSpacingMedium) {
                ForEach(Array(notificationViewModel.notificationList.enumerated()), id: \.offset) { _, notification in
                    NotificationCardView(
                        notification: notification,
                        isUnread: notificationViewModel.isUnread(notification),
                        tagColor: getTagColor
                    )
                    .contentShape(RoundedRectangle(cornerRadius: AppRadius.medium))
                    .onTapGesture {
                        notificationViewModel.markAsRead(notification)
                    }
                }
            }
            .padding(.horizontal, AppSpacing.screenHorizontalPadding)
            .padding(.top, AppSpacing.sm)
            .padding(.bottom, AppSpacing.xxl)
        }
        .refreshable {
            try? await notificationViewModel.getNotificationList()
        }
    }

    private func getTagColor(_ tagName: String) -> Color {
        switch tagName {
        case "Pending":
            return Color.notification
        case "Contacted":
            return Color.primaryButton1
        case "Negotiation":
            return Color.negotiation
        case "Finalization":
            return Color.present
        case "Deal Closed":
            return Color.dealClosed
        case "Order Processed":
            return Color.resumeButton
        default:
            return Color.subText
        }
    }
}

private struct NotificationCardView: View {
    let notification: PreviousTask
    let isUnread: Bool
    let tagColor: (String) -> Color

    private var status: (title: String, color: Color) {
        switch notification.taskApproveStatus {
        case 0:
            return ("Pending", Color.notification)
        case 1:
            return ("Started", Color.primaryButton1)
        case 2:
            return ("Paused", Color.absent)
        case 3:
            return ("Resumed", Color.resumeButton)
        case 4:
            return ("Finished", Color.present)
        case 5:
            return ("Deleted", Color.subText)
        default:
            return ("Status \(notification.taskApproveStatus)", Color.subText)
        }
    }

    private var titleText: String {
        let title = notification.taskName.trimmingCharacters(in: .whitespacesAndNewlines)
        return title.isEmpty ? "Task" : title
    }

    private var descriptionText: String {
        notification.taskDescription.trimmingCharacters(in: .whitespacesAndNewlines)
    }

    private var formattedDate: String {
        FormatterHelper.shared.formattedDateWithDay(from: notification.date)
    }

    private var formattedTimeRange: String {
        let start = FormatterHelper.shared.checkTimeFormatter(from: notification.startTime) ?? notification.startTime
        let end = FormatterHelper.shared.checkTimeFormatter(from: notification.endTime) ?? notification.endTime
        return "\(start) - \(end)"
    }

    private var actualTimeText: String? {
        guard notification.empStartTime != nil || notification.empEndTime != nil else {
            return nil
        }

        let start = formattedTime(notification.empStartTime) ?? "--"
        let end = formattedTime(notification.empEndTime) ?? "In progress"
        return "\(start) - \(end)"
    }

    private var valueText: String? {
        guard let amount = notification.value?.amount else { return nil }
        let currency = notification.value?.currency?.trimmingCharacters(in: .whitespacesAndNewlines) ?? ""
        return currency.isEmpty ? "\(amount)" : "\(currency) \(amount)"
    }

    private var volumeText: String? {
        guard let taskVolume = notification.taskVolume else { return nil }
        return "\(taskVolume)"
    }

    private var clientText: String? {
        guard let clientName = notification.clientName?.trimmingCharacters(in: .whitespacesAndNewlines),
              !clientName.isEmpty else {
            let clientID = notification.clientID.trimmingCharacters(in: .whitespacesAndNewlines)
            return clientID.isEmpty ? nil : "ID \(shortIdentifier(clientID))"
        }
        return clientName
    }

    private var recurrenceText: String? {
        guard notification.recurrenceID != nil || notification.recurrenceDetails != nil else {
            return nil
        }

        guard let details = notification.recurrenceDetails else {
            return "Recurring"
        }

        var parts: [String] = []

        if let cycle = details.taskCycle {
            switch cycle {
            case 0:
                parts.append("One-time")
            case 1:
                parts.append("Recurring")
            default:
                parts.append("Cycle \(cycle)")
            }
        }

        if let daysOfWeek = details.daysOfWeek, !daysOfWeek.isEmpty {
            parts.append(daysOfWeek.joined(separator: ", "))
        }

        if let startDate = formattedOptionalDate(details.startDate),
           let endDate = formattedOptionalDate(details.endDate) {
            parts.append("\(startDate) to \(endDate)")
        }

        return parts.isEmpty ? "Recurring" : parts.joined(separator: " | ")
    }

    private var addressText: String? {
        let addressParts = [notification.address1, notification.address2, notification.city]
            .compactMap { $0?.trimmingCharacters(in: .whitespacesAndNewlines) }
            .filter { !$0.isEmpty }

        return addressParts.isEmpty ? nil : addressParts.joined(separator: ", ")
    }

    private var updatedText: String {
        let formatted = FormatterHelper.shared.formattedFullYearDate(from: notification.updatedAt)
        return formatted.isEmpty ? notification.updatedAt : formatted
    }

    private var attachmentText: String? {
        let fileCount = notification.files.count
        let imageCount = notification.images.count

        guard fileCount > 0 || imageCount > 0 else { return nil }

        let fileText = fileCount == 1 ? "1 file" : "\(fileCount) files"
        let imageText = imageCount == 1 ? "1 photo" : "\(imageCount) photos"

        if fileCount > 0 && imageCount > 0 {
            return "\(fileText) | \(imageText)"
        } else if fileCount > 0 {
            return fileText
        } else {
            return imageText
        }
    }

    private var visibleTagLogs: [TagLog] {
        notification.tagLogs.filter { tagLog in
            guard let tagName = tagLog.tagName?.trimmingCharacters(in: .whitespacesAndNewlines) else {
                return false
            }
            return !tagName.isEmpty
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: AppSpacing.stackSpacingDefault) {
            HStack(alignment: .top, spacing: AppSpacing.stackSpacingDefault) {
                unreadIndicator
                    .padding(.top, AppSpacing.xs)

                VStack(alignment: .leading, spacing: AppSpacing.xs) {
                    Text(titleText)
                        .font(AppFont.primary(size: AppFont.Size.subheadline))
                        .fontWeight(AppFont.Weight.semibold)
                        .foregroundStyle(Color.taskClientName)
                        .lineLimit(2)

                    Text("Updated \(updatedText)")
                        .font(AppFont.primary(size: AppFont.Size.xSmall))
                        .fontWeight(AppFont.Weight.medium)
                        .foregroundStyle(Color.subText)
                        .lineLimit(1)
                }

                Spacer(minLength: AppSpacing.sm)

                NotificationStatusPill(title: status.title, color: status.color)
            }

            if !descriptionText.isEmpty {
                Text(descriptionText)
                    .font(AppFont.primary(size: AppFont.Size.caption))
                    .fontWeight(AppFont.Weight.medium)
                    .foregroundStyle(Color.subText)
                    .lineLimit(3)
                    .fixedSize(horizontal: false, vertical: true)
            }

            LazyVGrid(
                columns: [
                    GridItem(.flexible(), alignment: .leading),
                    GridItem(.flexible(), alignment: .leading)
                ],
                spacing: AppSpacing.stackSpacingDefault
            ) {
                NotificationInfoLine(icon: AppIcons.calendar, title: "Date", value: formattedDate)
                NotificationInfoLine(icon: AppIcons.clock, title: "Time", value: formattedTimeRange)

                if let actualTimeText {
                    NotificationInfoLine(icon: AppIcons.stopwatch, title: "Actual", value: actualTimeText)
                }

                if let valueText {
                    NotificationInfoLine(icon: AppIcons.value, title: "Value", value: valueText)
                }

                if let volumeText {
                    NotificationInfoLine(icon: AppIcons.volume, title: "Volume", value: volumeText)
                }

                if let recurrenceText {
                    NotificationInfoLine(icon: AppIcons.refresh, title: "Repeat", value: recurrenceText)
                }

                if let attachmentText {
                    NotificationInfoLine(icon: AppIcons.attachment, title: "Attachments", value: attachmentText)
                }
            }

            if let clientText {
                NotificationInfoLine(icon: AppIcons.user, title: "Client", value: clientText)
            }

            if let addressText {
                NotificationInfoLine(icon: AppIcons.location, title: "Location", value: addressText)
            }

            if !visibleTagLogs.isEmpty {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: AppSpacing.stackSpacingDefault) {
                        ForEach(Array(visibleTagLogs.enumerated()), id: \.offset) { _, tagLog in
                            if let tagName = tagLog.tagName {
                                NotificationTagChip(
                                    title: tagName,
                                    time: formattedTagTime(tagLog.time),
                                    color: tagColor(tagName)
                                )
                            }
                        }
                    }
                }
            }
        }
        .padding(AppSpacing.md)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.medium))
        .overlay {
            RoundedRectangle(cornerRadius: AppRadius.medium)
                .stroke(isUnread ? Color.primaryButton1.opacity(0.35) : Color.black.opacity(0.05), lineWidth: 1)
        }
        .shadow(color: .black.opacity(0.06), radius: 10, x: 0, y: 4)
        .accessibilityElement(children: .combine)
    }

    private var unreadIndicator: some View {
        ZStack {
            Circle()
                .fill(isUnread ? Color.taskSearchBar.opacity(0.3) : Color.clear)
                .frame(width: 12, height: 12)

            Circle()
                .fill(isUnread ? Color.primaryButton1 : Color.clear)
                .frame(width: 7, height: 7)
        }
    }

    private func formattedTagTime(_ value: String?) -> String? {
        guard let value,
              !value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            return nil
        }

        return FormatterHelper.shared.checkTimeFormatter(from: value) ?? value
    }

    private func formattedTime(_ value: String?) -> String? {
        guard let value,
              !value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            return nil
        }

        return FormatterHelper.shared.checkTimeFormatter(from: value) ?? value
    }

    private func formattedOptionalDate(_ value: String?) -> String? {
        guard let value,
              !value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else {
            return nil
        }

        return FormatterHelper.shared.formattedDateWithDay(from: value)
    }

    private func shortIdentifier(_ value: String) -> String {
        guard value.count > 12 else { return value }
        return "\(value.prefix(8))...\(value.suffix(4))"
    }
}

private struct NotificationStatusPill: View {
    let title: String
    let color: Color

    var body: some View {
        Text(title)
            .font(AppFont.primary(size: AppFont.Size.xSmall))
            .fontWeight(AppFont.Weight.semibold)
            .foregroundStyle(color)
            .lineLimit(1)
            .padding(.horizontal, AppSpacing.sm)
            .frame(minHeight: 26)
            .background(color.opacity(0.12))
            .clipShape(Capsule())
    }
}

private struct NotificationInfoLine: View {
    let icon: String
    let title: String
    let value: String

    var body: some View {
        HStack(alignment: .top, spacing: AppSpacing.compactIconTextSpacing) {
            Image(systemName: icon)
                .font(AppFont.primary(size: AppFont.Size.caption, weight: AppFont.Weight.semibold))
                .foregroundStyle(Color.primaryButton1)
                .frame(width: 16, height: 16)

            VStack(alignment: .leading, spacing: AppSpacing.xxs) {
                Text(title)
                    .font(AppFont.primary(size: AppFont.Size.micro))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.subText)
                    .lineLimit(1)

                Text(value)
                    .font(AppFont.primary(size: AppFont.Size.footnote))
                    .fontWeight(AppFont.Weight.semibold)
                    .foregroundStyle(Color.taskClientName)
                    .lineLimit(2)
                    .fixedSize(horizontal: false, vertical: true)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

private struct NotificationTagChip: View {
    let title: String
    let time: String?
    let color: Color

    var body: some View {
        HStack(spacing: AppSpacing.compactIconTextSpacing) {
            Circle()
                .fill(Color.white.opacity(0.9))
                .frame(width: 6, height: 6)

            VStack(alignment: .leading, spacing: AppSpacing.xxs) {
                Text(title)
                    .font(AppFont.primary(size: AppFont.Size.xSmall))
                    .fontWeight(AppFont.Weight.semibold)
                    .lineLimit(1)

                if let time {
                    Text(time)
                        .font(AppFont.primary(size: AppFont.Size.nano))
                        .fontWeight(AppFont.Weight.medium)
                        .lineLimit(1)
                }
            }
        }
        .foregroundStyle(Color.white)
        .padding(.horizontal, AppSpacing.sm)
        .frame(minHeight: 32)
        .background(color)
        .clipShape(Capsule())
    }
}

#Preview {
    NotificationView(notificationViewModel: NotificationViewModel())
}
