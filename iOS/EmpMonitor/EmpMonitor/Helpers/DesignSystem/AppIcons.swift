//
//  AppIcons.swift
//  EmpMonitor
//
//  Centralized system icon references using SF Symbols
//  Replace all hardcoded systemName strings with these constants
//

import SwiftUI

enum AppIcons {
    // MARK: - Navigation Icons

    static let close = "xmark"
    static let back = "chevron.left"
    static let forward = "chevron.right"
    static let down = "chevron.down"
    static let up = "chevron.up"
    static let menu = "line.3.horizontal"
    static let home = "house.fill"

    // MARK: - Action Icons

    static let search = "magnifyingglass"
    static let filter = "slider.horizontal.3"
    static let sort = "arrow.up.arrow.down"
    static let add = "plus"
    static let edit = "pencil"
    static let delete = "trash"
    static let save = "checkmark"
    static let cancel = "xmark.circle"

    // MARK: - Status Icons

    static let checkmark = "checkmark.circle.fill"
    static let warning = "exclamationmark.circle"
    static let error = "xmark.circle.fill"
    static let info = "info.circle"
    static let success = "checkmark.circle.fill"
    static let offline = "wifi.slash"

    // MARK: - Time/Calendar Icons

    static let clock = "clock"
    static let calendar = "calendar"
    static let time = "clock.badge"
    static let stopwatch = "stopwatch"

    // MARK: - User/Profile Icons

    static let user = "person.fill"
    static let users = "person.2.fill"
    static let userAdd = "person.badge.plus"
    static let profile = "person.crop.circle"
    static let account = "person.fill"

    // MARK: - Communication Icons

    static let message = "message.fill"
    static let phone = "phone.fill"
    static let call = "phone.fill"
    static let mail = "envelope.fill"
    static let link = "link"

    // MARK: - Location Icons

    static let location = "location.fill"
    static let map = "map.fill"
    static let pin = "mappin.circle.fill"
    static let compass = "location.circle.fill"

    // MARK: - File/Document Icons

    static let document = "doc.fill"
    static let folder = "folder.fill"
    static let attachment = "paperclip"
    static let download = "arrow.down.doc"
    static let upload = "arrow.up.doc"
    static let share = "square.and.arrow.up"

    // MARK: - Settings/System Icons

    static let settings = "gear"
    static let notification = "bell.fill"
    static let mute = "bell.slash.fill"
    static let notificationOff = "bell.slash"
    static let sound = "speaker.fill"

    // MARK: - View Options

    static let list = "list.bullet"
    static let grid = "square.grid.2x2"
    static let detail = "rectangle.3.offgrid"

    // MARK: - Content Icons

    static let image = "photo"
    static let camera = "camera.fill"
    static let video = "video.fill"
    static let play = "play.circle.fill"
    static let pause = "pause.circle.fill"
    static let value = "creditcard.fill"
    static let volume = "number.circle"

    // MARK: - Status/Activity Icons

    static let loading = "hourglass"
    static let sync = "arrow.2.circlepath"
    static let refresh = "arrow.circlepath"

    // MARK: - Utility

    static func image(named: String) -> String {
        named  // Keep reference for custom images
    }

    // MARK: - Migration Guide

    /*
     Replace hardcoded SF Symbol names as follows:

     // Old code:
     Image(systemName: "xmark")

     // New code:
     Image(systemName: AppIcons.close)

     Benefits:
     - Single source of truth for icon references
     - Consistent icon usage across app
     - Easy to swap icons globally
     - Compile-time checking (typos caught)
     - Self-documenting icon purposes

     Example Usage:
     Button(action: { }) {
         Image(systemName: AppIcons.delete)
     }
     .accessibilityLabel("Delete")
    */
}
