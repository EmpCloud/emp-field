//
//  ProfileLargeView.swift
//  EmpMonitor
//
//  Created by Sambhav Globussoft on 24/06/24.
//

import SwiftUI

struct ProfileLargeView: View {
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    var body: some View {
        ZStack {
            if let uiImage = profileImageLoader.profileImage {
                Image(uiImage: uiImage)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: 99, height: 99)
                    .clipShape(Circle())
            }else{
                Image(systemName: "person.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 27, height: 27)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(width: 99, height: 99)
                    .background(Color.white)
                    .clipShape(Circle())
            }
        }
//        .padding()
    }
}

struct ProfileLargeWithCameraView: View {
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    @ObservedObject var profileCameraViewModel: ProfileCameraViewModel
    
    @Binding var showCameraPermissionAlert: Bool
    
    @Binding var showProfileCamera: Bool
    
//    var imageURL: URL?
    
    var body: some View {
        ZStack {
            if let uiImage = profileImageLoader.profileImage {
                Image(uiImage: uiImage)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: 99, height: 99)
                    .clipShape(Circle())
            }else{
                Image(systemName: "person.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 27.5, height: 27.5)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(width: 99, height: 99)
                    .background(Color.white)
                    .clipShape(Circle())
//                    .background(
//                        Circle()
//                            .foregroundStyle(.white)
//                            .frame(width: 99, height: 99)
//                    )
//                ProgressView()
            }
            
            HStack{
                Circle()
                    .frame(width: 31, height: 31, alignment: .bottomTrailing)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .overlay {
                        Image(.cameraIcon)
                    }
                    .onTapGesture {
                        profileCameraViewModel.checkPermissions()
                        
                        if profileCameraViewModel.cameraAuthStatus {
                            showProfileCamera.toggle()
                        }else {
                            showCameraPermissionAlert.toggle()
                        }
                        
                    }
            }
            .frame(width: 100, height: 100, alignment: .bottomTrailing)
        }
    }
}



struct ProfileMediumView: View {
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    var body: some View {
        ZStack {
            if let uiImage = profileImageLoader.profileImage {
                Image(uiImage: uiImage)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: 72, height: 72)
                    .clipShape(Circle())
            }else{
                Image(systemName: "person.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 21, height: 21)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(width: 72, height: 72)
                    .background(Color.white)
                    .clipShape(Circle())
//                    .background(
//                        Circle()
//                            .foregroundStyle(.white)
//                            .frame(width: 72, height: 72)
//                    )
//                ProgressView()
            }
        }
    }
}

struct ProfileSmallView: View {
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    var body: some View {
        ZStack {
            if let uiImage = profileImageLoader.profileImage {
                Image(uiImage: uiImage)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: 40, height: 40)
                    .clipShape(Circle())
            }else{
                Image(systemName: "person.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 14.12, height: 14.12)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .frame(width: 40, height: 40)
                    .background(Color.white)
                    .clipShape(Circle())
//                    .background(
//                        Circle()
//                            .foregroundStyle(.white)
//                            .frame(width: 40, height: 40)
//                    )
//                
//                ProgressView()
            }
        }
//        .onAppear{
//        }
    }
}


struct ProfileMediumDarkView: View {
    
    @EnvironmentObject var profileImageLoader: ProfileImageLoader
    
    var body: some View {
        ZStack {
            if let uiImage = profileImageLoader.profileImage {
                Image(uiImage: uiImage)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: 72, height: 72)
                    .clipShape(Circle())
            }else{
                Image(systemName: "person.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 21, height: 21)
                    .foregroundStyle(Color.white)
                    .frame(width: 72, height: 72)
                    .background(Color.profileDarkBg)
                    .clipShape(Circle())
//                    .background(
//                        Circle()
//                            .foregroundStyle(.profileDarkBg)
//                            .frame(width: 72, height: 72)
//                    )
//                
//                ProgressView()
            }
        }
    }
}

struct ClientProfileSmallDarkView: View {
    
//    @ObservedObject var profileImageLoader: ProfileImageLoader
    
//    var clientProfilePicURL: String
    var clientProfilePic: UIImage?
    
    var body: some View {
        ZStack {
            if let uiImage = clientProfilePic {
                Image(uiImage: uiImage)
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: 44, height: 44)
                    .clipShape(Circle())
                    .padding(-10)
            }else {
                Image(systemName: "person.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 18.86, height: 27)
                    .foregroundStyle(Color.white)
                    .background(
                        Circle()
                            .foregroundStyle(Color.profileDarkBg)
                            .frame(width: 44, height: 44)
                           

                    )

            }
            
            //                .padding()
//                .padding(.top, 20)
        }
        .onAppear {
//            profileImageLoader.loadClientProfileImage(clientImageURL: clientProfilePicURL)
//            self.clientProfilePic = profileImageLoader.clientProfileImage
        }
//        .frame(maxWidth: .infinity, maxHeight: .infinity)
//        .background(Color.black)
    }
}

struct ClientProfileLargeWithCameraView: View {
    
    @Binding var showProfileCamera: Bool
    
    @State var clientProfilePic: UIImage?
    
//    var imageURL: URL?
    
    var body: some View {
        ZStack {
           if let uiImage = clientProfilePic {
            Image(uiImage: uiImage)
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(width: 99, height: 99)
                .clipShape(Circle())
//                .padding(-10)
           }else{
                Image(systemName: "person.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 27.5, height: 27.5)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .background(
                        Circle()
                            .foregroundStyle(.white)
                            .frame(width: 99, height: 99)
                    )
            }
            
            HStack{
                Circle()
                    .frame(width: 31, height: 31, alignment: .bottomTrailing)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .overlay {
                        Image(.cameraIcon)
                    }
                    .onTapGesture {
                        showProfileCamera.toggle()
                    }
            }
            .frame(width: 100, height: 100, alignment: .bottomTrailing)
        }
    }
}

struct ClientProfileLargeView: View {
    var body: some View {
        ZStack {
//            if let imageData = UserDefaults.standard.data(forKey: "ProfilePic"), let uiImage = UIImage(data: imageData) {
//                Image(uiImage: uiImage)
//                    .resizable()
//                    .aspectRatio(contentMode: .fill)
//                    .frame(width: 99, height: 99)
//                    .clipShape(Circle())
//            }else{
                Image(systemName: "person.fill")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 18, height: 18)
                    .foregroundStyle(
                        LinearGradient(gradient: Gradient(colors: [Color.primaryButton1, Color.primaryButton2]), startPoint: .top, endPoint: .bottom)
                    )
                    .background(
                        Circle()
                            .foregroundStyle(.white)
                            .frame(width: 72, height: 72)
                    )
                
//                ProgressView()
//            }
        }
    }
}

#Preview {
    ProfileMediumDarkView()
}
#Preview {
    ProfileLargeView()
        .frame(width: 500, height: 500)
        .background(Color.black)
}
#Preview {
    ProfileLargeWithCameraView(profileCameraViewModel: ProfileCameraViewModel(), showCameraPermissionAlert: .constant(false), showProfileCamera: .constant(false))
        .frame(width: 500, height: 500)
        .background(Color.black)
}
#Preview {
    ProfileMediumView()
        .frame(width: 500, height: 500)
        .background(Color.black)
}
#Preview {
    ProfileSmallView()
        .frame(width: 500, height: 500)
        .background(Color.black)
}



