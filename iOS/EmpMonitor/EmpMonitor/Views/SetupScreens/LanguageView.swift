//
//  LanguageView.swift
//  EmpMonitor
//
//  Created by Sumit Ghosh on 18/06/24.
//

//import SwiftUI
//
//struct LanguageView: View {
//    
//    @EnvironmentObject var profileImageLoader: ProfileImageLoader
//    
//    @State private var languages: [String] = [
//        "English",
//        "हिंदी",
//        "அறியும்",
//        "ಸಾಕುಪ್ರಾಣಿಯ",
//        "ചെയ്യും",
//        "Indonesian",
//        "একটি",
//        "डोळे",
//    ]
//    
//    @State private var selectedLanguage: String = ""
//    
//    @State private var nextScreen: Bool = false
//    
//    
//    var body: some View {
//        ZStack(alignment: .top) {
//            Image(.topSetupScreenBg)
//            
//            VStack(alignment: .center, spacing: 40){
//                    
//                    Text("Choose your Language")
//                        .font(.custom("Montserrat", size: 24))
//                        .foregroundStyle(Color.subText)
//                        .padding(.top, 130)
//                
//                
//                //MARK: Language Options
//                VStack(spacing: 15) {
//                    
//                    ForEach(languages, id: \.self){ language in
//                        RoundedRectangle(cornerRadius: 10)
//                            .fill(Color.white)
//                            .frame(height: 50)
//                            .overlay {
//                                HStack{
//                                    Text(language)
//                                        .font(.custom("Montserrat", size: 18))
//                                        .foregroundStyle(Color.subText)
//                                    Spacer()
//                                    Button(action: { selectedLanguage = language }, label: {
//                                        if selectedLanguage == language {
//                                            Image(systemName: "checkmark.circle.fill" )
//                                                .foregroundStyle(Color.primaryButton1)
//                                        }
//                                        else{
//                                            Image(systemName: "circle" )
//                                                .foregroundStyle(Color.strokeGray)
//                                        }
//                                    })
//                                }
//                                .padding(.leading)
//                                .padding(.trailing)
//                                .onTapGesture {
//                                    selectedLanguage = language
//                                }
//                            }
//                            .overlay{
//                                RoundedRectangle(cornerRadius: 10)
//                                    .stroke(selectedLanguage == language ? Color.primaryButton1 : Color.strokeGray)
//                            }
//                            .onTapGesture {
//                                selectedLanguage =  language
//                            }
//                            .padding(.horizontal, 60)
//                    }
//                }
//                
//                
//                //MARK: Next Button
//                PrimaryButton(text: "Next") {
//                    if selectedLanguage != "" {
//                        nextScreen = true
//                    }
//                }
//                .padding(.horizontal, 50)
//            }
//        }
//         .ignoresSafeArea(.all)
//         .navigationDestination(isPresented: $nextScreen) {
//             SetupTabView()
//                 .environmentObject(profileImageLoader)
//                 .navigationBarBackButtonHidden()
//         }
//    }
//}
//#Preview {
//    LanguageView()
//}
