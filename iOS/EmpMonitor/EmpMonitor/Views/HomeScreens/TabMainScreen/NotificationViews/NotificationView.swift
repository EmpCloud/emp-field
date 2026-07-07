//
//  NotificationView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 09/09/24.
//

import SwiftUI

struct NotificationView: View {
    
    @Environment(\.dismiss) var dismiss
    
    @StateObject private var notificationViewModel = NotificationViewModel()
    
    @State private var animatedIndex = 0
    
    var body: some View {
        ZStack {
            LinearGradient(gradient: Gradient(colors: [Color.appBg1, Color.appBg2]), startPoint: .top, endPoint: .bottom)
                .ignoresSafeArea()
            
            RoundedRectangle(cornerRadius: 20)
                .fill(Color.rectangleBG)
                .padding(.top)
                .ignoresSafeArea(edges: .bottom)
                .shadow(color: .black.opacity(0.25), radius: 12)
                .overlay(alignment: .top) {
                    VStack(alignment: .leading) {
                        HStack {
                            Spacer()
                            if !notificationViewModel.notificationList.isEmpty {
                                Button {
                                    animateSlideOut()
                                } label: {
                                    Text("Read All")
                                        .font(.custom("Montserrat", size: 12))
                                        .fontWeight(.semibold)
                                        .underline()
                                }
                                .padding()
                                .padding(.top)
                            } else {
                                Spacer().frame(height: 50)
                            }
                        }

                        if notificationViewModel.isLoading {
                            // Loading state
                            VStack {
                                Spacer()
                                ProgressView()
                                    .progressViewStyle(.circular)
                                    .scaleEffect(1.2)
                                Text("Loading notifications…")
                                    .font(.custom("Montserrat", size: 13))
                                    .foregroundStyle(Color.secondary)
                                    .padding(.top, 8)
                                Spacer()
                            }
                            .frame(maxWidth: .infinity, maxHeight: .infinity)

                        } else if let error = notificationViewModel.error {
                            // Error state
                            VStack(spacing: 12) {
                                Spacer()
                                Image(systemName: "wifi.slash")
                                    .font(.system(size: 36))
                                    .foregroundStyle(Color.secondary)
                                Text("Failed to load notifications")
                                    .font(.custom("Montserrat", size: 14))
                                    .fontWeight(.semibold)
                                    .foregroundStyle(Color.primary)
                                Text(error.localizedDescription)
                                    .font(.custom("Montserrat", size: 12))
                                    .foregroundStyle(Color.secondary)
                                    .multilineTextAlignment(.center)
                                    .padding(.horizontal, 24)
                                Button {
                                    Task { try? await notificationViewModel.getNotificationList() }
                                } label: {
                                    Text("Retry")
                                        .font(.custom("Montserrat", size: 13))
                                        .fontWeight(.semibold)
                                        .foregroundStyle(.white)
                                        .padding(.horizontal, 24)
                                        .padding(.vertical, 8)
                                        .background(Color.primaryButton1)
                                        .clipShape(Capsule())
                                }
                                Spacer()
                            }
                            .frame(maxWidth: .infinity, maxHeight: .infinity)

                        } else if notificationViewModel.notificationList.isEmpty {
                            // Empty state
                            VStack(spacing: 12) {
                                Spacer()
                                Image(systemName: "bell.slash")
                                    .font(.system(size: 36))
                                    .foregroundStyle(Color.secondary)
                                Text("No notifications")
                                    .font(.custom("Montserrat", size: 14))
                                    .fontWeight(.semibold)
                                    .foregroundStyle(Color.primary)
                                Text("You're all caught up!")
                                    .font(.custom("Montserrat", size: 12))
                                    .foregroundStyle(Color.secondary)
                                Spacer()
                            }
                            .frame(maxWidth: .infinity, maxHeight: .infinity)

                        } else {
                            // Notification list
                            ScrollView {
                                VStack {
                                    ForEach(Array(notificationViewModel.notificationList.enumerated()), id: \.element.id) { index, notification in
                                        VStack(alignment: .leading) {
                                            HStack {
                                                Circle()
                                                    .fill(Color.taskSearchBar.opacity(0.3))
                                                    .frame(width: 11, height: 11)
                                                    .overlay {
                                                        Circle()
                                                            .fill(Color.primaryButton1)
                                                            .frame(width: 7, height: 7)
                                                    }
                                                Text(notification.taskName)
                                                    .font(.custom("Montserrat", size: 14))
                                                    .fontWeight(.semibold)

                                                Spacer()

                                                if let tagName = notification.tagLogs.first?.tagName {
                                                    NotificationTypeBoxView(text: tagName, bgColor: getTagColor(tagName))
                                                }
                                            }

                                            Text(notification.taskDescription)
                                                .font(.custom("Montserrat", size: 10))
                                                .fontWeight(.medium)
                                                .padding(.horizontal, 20)
                                                .padding(.trailing, 90)
                                        }
                                        .padding(.horizontal)
                                        .padding(.bottom)
                                        .offset(x: index < animatedIndex ? UIScreen.main.bounds.width : 0)
                                        .animation(.easeIn(duration: 0.3).delay(Double(index) * 0.01), value: animatedIndex)
                                    }
                                }
                            }
                        }
                    }
                }
   
            
        }
        .onAppear {
            Task {
                try await notificationViewModel.getNotificationList()
            }
        }
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
//                HStack {
                    BackButtonView()
                        .onTapGesture {
                            dismiss()
//                        }
                    
                }
            }
            ToolbarItem(placement: .principal) {
                Text("Notifications")
                    .font(.custom("Montserrat", size: 20))
                    .fontWeight(.semibold)
                    .foregroundStyle(Color.white)
//                        .padding(.horizontal, 70)
            }
        }
    }
    
    private func animateSlideOut() {
        guard animatedIndex < notificationViewModel.notificationList.count else {
            notificationViewModel.notificationList.removeAll()
            return
        }
        
        withAnimation {
            animatedIndex += 1
        }
        
        //Recursive call to animate each item with a delay
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
            animateSlideOut()
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
            return Color.clear
        }
    }
}

#Preview {
    NotificationView()
}
