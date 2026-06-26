//
//  ClientCardFlipAnimationViewModel.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 28/08/24.
//

import Foundation

class ClientCardFlipAnimationViewModel: ObservableObject {
    @Published var isFlipped: Bool = false
    
    func flipButtonTapped() {
        isFlipped.toggle()
    }
}
