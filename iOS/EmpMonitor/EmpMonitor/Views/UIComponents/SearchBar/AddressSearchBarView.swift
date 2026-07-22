//
//  AddressSearchBarView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 24/06/24.
//

import SwiftUI

struct AddressSearchBarView: View {
    
    @State var searchText: String = ""
    @State var isSearching: Bool = false
    @State var searchPlaceholder: String = ""
    
    
    
    var body: some View {
        HStack(spacing: AppSpacing.iconTextSpacing) {
            Image(systemName: "magnifyingglass")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: AppLayout.iconSmall, height: AppLayout.iconSmall)
                .foregroundStyle(Color.searchIcon)

            Text("Search for area, street name...")
                .font(AppFont.primary(size: AppFont.Size.caption))
                .foregroundStyle(Color.mapSearchBarText)
                .lineLimit(1)
                .minimumScaleFactor(0.85)

            Spacer(minLength: AppSpacing.zero)
        }
        .padding(.horizontal, AppSpacing.md)
        .frame(maxWidth: .infinity)
        .frame(minHeight: AppLayout.minimumTouchTarget)
        .background(
            LinearGradient(gradient: Gradient(colors: [Color.blueGradient1, Color.blueGradient2]), startPoint: .top, endPoint: .bottom)
        )
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.small))
    }
}

#Preview {
    AddressSearchBarView()
}
