//
//  MapSearchBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct MapSearchBarView: View {
    @Binding var searchText: String
    var body: some View {
        HStack(spacing: AppSpacing.iconTextSpacing) {
            Image(systemName: "magnifyingglass")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: AppLayout.iconSmall, height: AppLayout.iconSmall)
                .foregroundStyle(Color.searchIcon)

            TextField(text: $searchText) {
                Text("Search for area, street name...")
                    .font(AppFont.primary(size: AppFont.Size.caption))
                    .foregroundStyle(Color.mapSearchBarText)
            }
            .font(AppFont.primary(size: AppFont.Size.caption))
            .foregroundStyle(Color.mapSearchBarText)
        }
        .padding(.horizontal, AppSpacing.md)
        .frame(maxWidth: .infinity)
        .frame(minHeight: AppLayout.minimumTouchTarget)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
//        .frame(maxWidth: .infinity, maxHeight: .infinity)
//        .background(Color.black)
    }
}

#Preview {
    MapSearchBarView(searchText: .constant(""))
}
