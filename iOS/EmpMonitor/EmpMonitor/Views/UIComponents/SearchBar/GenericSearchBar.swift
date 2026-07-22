//
//  GenericSearchBar.swift
//  EmpMonitor
//
//  Unified search bar component with optional filter
//  Replaces: ClientSearchBarView, TaskSearchBarView
//

import SwiftUI

struct GenericSearchBar: View {
    @Binding var searchText: String
    @Binding var showFilter: Bool

    let placeholder: String
    let bgColor: Color
    let showFilterButton: Bool
    let onFilterTap: (() -> Void)?

    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: AppRadius.small)
                .fill(bgColor)
                .frame(height: AppLayout.searchBarHeight)
                .frame(maxWidth: .infinity)

            HStack(spacing: AppSpacing.iconTextSpacing) {
                Image(systemName: "magnifyingglass")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: AppLayout.iconSmall, height: AppLayout.iconSmall)
                    .foregroundStyle(Color.white)
                    .accessibilityLabel("Search")

                TextField(text: $searchText) {
                    Text(placeholder)
                        .font(AppFont.primary(size: AppFont.Size.caption))
                        .foregroundStyle(Color.white.opacity(0.9))
                }
                    .font(AppFont.primary(size: AppFont.Size.caption))
                    .foregroundStyle(Color.white)

                if showFilterButton {
                    Spacer()

                    Button {
                        withAnimation(.spring) {
                            showFilter.toggle()
                        }
                        onFilterTap?()
                    } label: {
                        Image(systemName: "slider.horizontal.3")
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: AppLayout.iconSmall, height: AppLayout.iconSmall)
                            .padding(.trailing, AppSpacing.compactControlInnerPadding)
                    }
                    .frame(width: AppLayout.minimumTouchTarget, height: AppLayout.minimumTouchTarget)
                    .accessibilityLabel("Filter")
                    .accessibilityHint("Double tap to toggle filters")
                }
            }
            .padding(.horizontal, AppSpacing.screenHorizontalPadding)
        }
    }
}

#Preview {
    VStack(spacing: AppSpacing.stackSpacingMedium) {
        GenericSearchBar(
            searchText: .constant(""),
            showFilter: .constant(false),
            placeholder: "Search Clients",
            bgColor: Color.blue.opacity(0.7),
            showFilterButton: false,
            onFilterTap: nil
        )

        GenericSearchBar(
            searchText: .constant(""),
            showFilter: .constant(false),
            placeholder: "Search Tasks",
            bgColor: Color.blue.opacity(0.7),
            showFilterButton: true,
            onFilterTap: { AppLog.debug("Filter tapped") }
        )
    }
    .padding(AppSpacing.pagePadding)
}
