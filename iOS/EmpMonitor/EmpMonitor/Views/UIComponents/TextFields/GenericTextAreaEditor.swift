//
//  GenericTextAreaEditor.swift
//  EmpMonitor
//
//  Unified text area editor component
//  Replaces: LargeTextEditorView, AddTaskTextEditor (partial)
//

import SwiftUI

struct GenericTextAreaEditor: View {
    @Binding var text: String

    let placeholder: String
    let minHeight: CGFloat
    var maxHeight: CGFloat? = nil
    let showCharacterCount: Bool

    private var characterCount: Int { text.count }
    private var maxCharacters: Int = 500

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            ZStack(alignment: .topLeading) {
                RoundedRectangle(cornerRadius: 8)
                    .fill(Color.white)
                    .stroke(Color.gray.opacity(0.3), lineWidth: 1)

                TextEditor(text: $text)
                    .font(.system(size: 14, weight: .regular))
                    .padding(8)
                    .scrollContentBackground(.hidden)

                if text.isEmpty {
                    Text(placeholder)
                        .font(.system(size: 14, weight: .regular))
                        .foregroundStyle(Color.gray.opacity(0.5))
                        .padding(12)
                        .allowsHitTesting(false)
                }
            }
            .frame(minHeight: minHeight)
            .if(maxHeight != nil) { view in
                view.frame(maxHeight: maxHeight)
            }

            if showCharacterCount {
                Text("\(characterCount)/\(maxCharacters)")
                    .font(.system(size: 12, weight: .regular))
                    .foregroundStyle(characterCount > maxCharacters ? .red : .gray)
                    .frame(maxWidth: .infinity, alignment: .trailing)
            }
        }
        .accessibilityLabel(placeholder)
    }
}

extension View {
    @ViewBuilder
    func `if`<Content: View>(_ condition: Bool, transform: (Self) -> Content) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }
}

#Preview {
    VStack(spacing: 16) {
        GenericTextAreaEditor(
            text: .constant(""),
            placeholder: "Enter task description...",
            minHeight: 80,
            showCharacterCount: true
        )

        GenericTextAreaEditor(
            text: .constant(""),
            placeholder: "Enter notes...",
            minHeight: 100,
            maxHeight: 200,
            showCharacterCount: false
        )
    }
    .padding()
}
