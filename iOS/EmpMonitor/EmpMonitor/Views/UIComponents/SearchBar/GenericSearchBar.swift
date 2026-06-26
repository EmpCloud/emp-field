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
            RoundedRectangle(cornerRadius: 6)
                .fill(bgColor)
                .frame(height: 44)
                .frame(maxWidth: .infinity)

            HStack(spacing: 8) {
                Image(systemName: "magnifyingglass")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 20, height: 20)
                    .foregroundStyle(Color.white)
                    .accessibilityLabel("Search")

                TextField(placeholder, text: $searchText)
                    .font(.system(size: 14, weight: .regular))
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
                            .frame(width: 20, height: 20)
                            .padding(.trailing, 5)
                    }
                    .accessibilityLabel("Filter")
                    .accessibilityHint("Double tap to toggle filters")
                }
            }
            .padding(.horizontal)
        }
        .accessibilityElement(children: .combine)
    }
}

#Preview {
    VStack(spacing: 16) {
        GenericSearchBar(
            searchText: .constant(""),
            showFilter: .constant(false),
            placeholder: "Search clients...",
            bgColor: Color.blue.opacity(0.7),
            showFilterButton: false,
            onFilterTap: nil
        )

        GenericSearchBar(
            searchText: .constant(""),
            showFilter: .constant(false),
            placeholder: "Search tasks...",
            bgColor: Color.blue.opacity(0.7),
            showFilterButton: true,
            onFilterTap: { print("Filter tapped") }
        )
    }
    .padding()
}
