//
//  MobileLoginView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

import SwiftUI
import Combine

struct MobileLoginView: View {
    
    @State var presentSheet = false
    @State var countryCode: String = "+91"
    @State var countryFlag : String = "🇮🇳"
    @State private var searchCountry: String = ""
    @State private var countryPattern: String = "### ### ####"
    @State private var countryLimit: Int = 17
    
    @FocusState private var keyIsFocused: Bool
    
    let countries: [CPData] = Bundle.main.decode("CountryNumbers.json")
    
    @State private var mobileNumber: String = ""
    
    var filteredResorts: [CPData] {
        if searchCountry.isEmpty {
            return countries
        }else {
            return countries.filter { $0.name.contains(searchCountry) }
        }
    }
    
    var body: some View {
        ZStack(alignment: .top){
            Color.white
                .ignoresSafeArea(.all)
            
            Image(.loginScreenTopBg)
                .ignoresSafeArea(.all)
            
            VStack {
                
               //MARK: AppIcon
                CircularAppIcon()
                
                
                Text("Login")
                    .font(AppFont.primary(size: AppFont.Size.screenTitle))
                    .padding(.top, 150)
                
                ZStack(alignment: .leading){
                    //MARK: TextField

                    MobileTextField(text: $mobileNumber, placeholder: "Mobile Number")
                        .padding(.horizontal, 50)
                        .padding(.vertical)
                        .focused($keyIsFocused)
                        .onReceive(Just(mobileNumber)) { _ in
                            applyPatternOnNumbers(&mobileNumber, pattern: countryPattern, replacementCharacter: "#")
                        }
                        .toolbarDoneButton()
                    
                    //MARK: Country Flag and Code
                    Button{
                        presentSheet = true
                        keyIsFocused = false
                    }label: {
                        HStack(spacing: 10){
                            Text("\(countryFlag)")
                            Text("\(countryCode)")
                                .font(AppFont.primary(size: AppFont.Size.title3))
                                .foregroundStyle(Color.mobileText)
                        }
                        .frame(minHeight: AppLayout.minimumTouchTarget)
                        .contentShape(Rectangle())
                    }
                    .padding(.leading, 60)
                    .accessibilityLabel("Select country code")
                }
                
                //MARK: Send OTP button
                PrimaryButton(text: "Send OTP") {
                    // do something
                }
                .disableWithOpacity(mobileNumber.count < 4)
                .padding(.horizontal, 50)
                
            }
            

        }
        .sheet(isPresented: $presentSheet) {
            NavigationStack {
                List(filteredResorts) { country in
                    HStack {
                        Text(country.flag)
                        Text(country.name)
                            .font(AppFont.primary(size: AppFont.Size.subheadline))
                        Spacer()
                        Text(country.dial_code)
                    }
                    .onTapGesture {
                        self.countryFlag = country.flag
                        self.countryCode = country.dial_code
                        self.countryPattern = country.pattern
                        self.countryLimit = country.limit
                        presentSheet = false
                        searchCountry = ""
                    }
                }
                .listStyle(.plain)
                .searchable(text: $searchCountry, prompt: "Your country")
            }
            .presentationDetents([.medium, .large])
        }
        .ignoresSafeArea(.keyboard)
    }

    //MARK: Phone Number Pattern
    func applyPatternOnNumbers(_ stringvar: inout String, pattern: String, replacementCharacter: Character) {
        var pureNumber = stringvar.replacingOccurrences(of: "[^0-9", with: "", options: .regularExpression)
        for index in 0 ..< pattern.count {
            guard index < pureNumber.count else {
                stringvar = pureNumber
                return
            }
            let stringIndex = String.Index(utf16Offset: index, in: pattern)
            let patternCharacter = pattern[stringIndex]
            guard patternCharacter != replacementCharacter else {
                continue
            }
            pureNumber.insert(patternCharacter, at: stringIndex)
        }
        stringvar = pureNumber
    }
}

#Preview {
    MobileLoginView()
}
