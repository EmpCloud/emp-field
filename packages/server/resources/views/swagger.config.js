import swaggerAutogen from 'swagger-autogen';
const swagger = swaggerAutogen();
import config from 'config';

const doc = {
    info: {
        app_version: config.get('app_version') ?? '1.0.0',
        title: 'Empmonitor Field Tracking API`s', // by default: "REST API"
        description: 'Documentation', // by default: ""
    },
    host: process.env.swagger_host_url, // by default: "localhost:3000"
    basePath: '/', // by default: "/"
    schemes: process.env.NODE_ENV !== 'localDev' ? ['http'] : ['http'], // by default: ['http']
    consumes: ['application/json', 'application/x-www-form-urlencoded'],
    produces: ['application/json'],
    tags: [    
        // by default: empty Array
        // {
        //     name: 'Open', // Tag name
        //     description: 'Endpoints', // Tag description
        // },
        // {
        //     name: 'Activity', // Tag name
        //     description: 'Endpoints', // Tag description
        // },
    ],

    securityDefinitions: {
        AccessToken: {
            type: 'apiKey',
            in: 'header',
            name: 'x-access-token',
            description: 'Please provide the valid access token, if you dont have please login and get the token as response!',
        },
    }, // by default: empty object
    definitions: {
        updateSnapDetails:{
            snap_points_limit:100,
            snap_duration_limit:60
        },
        getIndividualAttendanceData:{
            empId:29017,
            start_date:'2024-09-19',
            end_date:'2024-09-19',
            skip:0,
            limit:10,
            allData:false
        },
        deleteUserPerm:{
            empIds: ["empId1", "empId2", "empId3"]
          },
        deleteCLientIds:{
            clientIds: ["64df3c7c0cc4be50828ae06b", "64df3c7c0cc4be50828ae06b", "64df3c7c0cc4be50828ae06b"]
          },          
        getEmployeeConf:{
            empId: "249907"
        },
        updateEmployeeConf:{
            empId:"249907",
            geoLogsStatus:true,
            frequency:20,
            isBioMetricEnabled:0,
            isMobileDeviceEnabled:1,
            isGeoFencingEnabled:0,
        },  
        getLocationList:{
            orgId:"245"
        },
        getGeoLocationDetails:{
            orgId:"245",
            locationName:"Bangalore"
        },
        updateGeoLocationDetails:{
            orgId:"246",
            locationName:"Bangalore",
            isMobEnabled:true,
            geo_fencing:true,
            latitude:"12.97400951850594",
            longitude:"77.64695398154025",
            radius:10,
        },
        updateLocation:{
            locationName:"Bangalore",
            address:"#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station",
            latitude:-4722387464,
            longitude:+347326842,
            range:500,
            geo_fencing:false,
            isMobEnabled:true
        },
        createEmployeeLocation:{
            employeeId:"64df3c7c0cc4be50828ae06b",
            address:"#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station",
            latitude:-4722387464,
            longitude:+347326842,
            range:500,
            geo_fencing:true,
            isMobEnabled:true
        },
        allOrgEmployee:{
            skip:0,
            limit:10
        },
        filterTask:{
            date:'2024-05-22',
            status:0,//0:all,1:current,2:finished
        },
        markAttendance:{
            time:'11:20:33',
            latitude:-4722387464,
            longitude:+347326842,
        },
        empEmployeeImport:{
            "usersData": [
                {
                    "organization_id":246,
                    "id":29017,
                    "fullName":"John Doe",
                    "phoneNumber":"91-7753829920",
                    "email":"test@gmail.com",
                    "address1":"#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station",
                    "department":"Devops",
                    "location":"belgum",
                    "timezone":"Africa/Johannesburg",
                    "role":"Employee",
                    "password":"TrackField@124"
                }
            ]
        },
        importEmpUsers:{
                "usersData": [
                    {
                      "id": 29017,
                      "u_id": 57968,
                      "first_name": "Rithika",
                      "name": "Rithika",
                      "last_name": "A",
                      "email": "rithika.r12@globussoft.in",
                      "phone": "91-8412579632",
                      "date_join": null,
                      "address": null,
                      "photo_path": "/default/profilePic/user.png",
                      "status": 1,
                      "organization_id": 246,
                      "location_id": 643,
                      "location": "belgum",
                      "department_id": 496,
                      "emp_code": "Ri24",
                      "shift_id": null,
                      "timezone": "Africa/Johannesburg",
                      "tracking_mode": 1,
                      "tracking_rule_type": 1,
                      "department": "Devops",
                      "role_id": 554,
                      "role": "Employee",
                      "role_type": 1,
                      "total_count": 207,
                      "full_name": "Rithika A",
                      "password": "Rithika@124",
                      "software_version": null,
                      "shift_name": null,
                      "shift_data": null,
                      "computer_name": null,
                      "username": null,
                      "domain": null,
                      "employee_unique_id": "rithika.r12@globussoft.in",
                      "project_name": "",
                      "roles": [
                        {
                          "role_id": 554,
                          "role": "Employee",
                          "role_type": 1
                        }
                      ],
                      "encriptedpassword": "00035c764975e1bbc4131cfc6c7a2171:b23a22f129469bffdac9abfe635163b4",
                      "assigned": [],
                      "importedStatus": true
                    }
                ]
        },
        adminImportUsers:{
            "usersData": [
                    {
                      "id": 29017,
                      "u_id": 57968,
                      "first_name": "Rithika",
                      "name": "Rithika",
                      "last_name": "A",
                      "email": "rithika.r12@globussoft.in",
                      "phone": "91-8412579632",
                      "date_join": null,
                      "address": null,
                      "photo_path": "/default/profilePic/user.png",
                      "status": 1,
                      "organization_id": 246,
                      "location_id": 643,
                      "location": "belgum",
                      "department_id": 496,
                      "emp_code": "Ri24",
                      "shift_id": null,
                      "timezone": "Africa/Johannesburg",
                      "tracking_mode": 1,
                      "tracking_rule_type": 1,
                      "department": "Devops",
                      "role_id": 554,
                      "role": "Employee",
                      "role_type": 1,
                      "total_count": 207,
                      "full_name": "Rithika A",
                      "password": "Rithika@124",
                      "software_version": null,
                      "shift_name": null,
                      "shift_data": null,
                      "computer_name": null,
                      "username": null,
                      "domain": null,
                      "employee_unique_id": "rithika.r12@globussoft.in",
                      "project_name": "",
                      "roles": [
                        {
                          "role_id": 554,
                          "role": "Employee",
                          "role_type": 1
                        }
                      ],
                      "encriptedpassword": "00035c764975e1bbc4131cfc6c7a2171:b23a22f129469bffdac9abfe635163b4",
                      "assigned": [],
                      "importedStatus": true
                    }
                ]
        },
        attendanceRequest:{
            date: '2024-05-22',
            check_in:'2024-05-21T21:30:00.000Z',
            check_out:'2024-05-21T21:30:00.000Z',
            reason:'Sick Leave'
        },
        trackUser:[{
            date: '2024-05-22',
            time:'11:20:33',
            latitude:-4722387464,
            longitude:+347326842,

        }],
        tempTrackUser:[{
            latitude:-4722387464,
            longitude:+347326842,

        }],
        userFilter:{
            date:"2024-07-05",
            checkIn:false,
            checkOut:false
        },
        userLocationFilter:{
            date:"2024-07-05"
        },
        clientDetails:{
            clientName:"John Doe",
            emailId:"aadarshbajpai@globussoft.in",
            contactNumber:'9926471094',
            clientProfilePic:null,
            category:'Sales',
            countryCode:'+91',
            address1:'#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station',
            address2:'Bangalore, Karnataka',
            country:'India',
            state:'Karnataka',
            city:'Bangalore',
            zipCode:'470004',
            latitude:-4722387464,
            longitude:+347326842,
            employeeIds:'653b53bf29c9492b32097b71'
        },
        CreateLeaves:{
            day_type: 2,
            leave_type: 2,
            start_date :'2024-05-22',
            end_date: '2024-05-22',
            reason: 'Some Example',
            // status: '',//o:pending,1:approved:2,3:rejected
        },
        UpdateLeaves:{
            leave_id: 999,
            day_type: 2,
            leave_type: 2,
            start_date :'2024-05-22',
            end_date: '2024-05-22',
            reason: 'Some Example',
        },
        DeleteLeaves:{
            leave_id: 999
        },
        AdminDetails: {
            fullName: 'John Dev',
            age: 25,
            gender: 'male',
            profilePic: null,
            password: 'John@emp123',
            countryCode: '+91',
            phoneNumber: '6787986543',
            email: 'john@globussoft.in',
            orgName: 'Globussoft Technologies',
            address1: '#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station',
            address2: 'Bangalore, Karnataka',
            city: 'Bangalore',
            state: 'Karnataka',
            country: 'India',
            zipCode: '560001',
            timezone: 'Asia/Kolkata'
        },
        getLeaves:{
            "startDate": "2024-08-01",
            "endDate": "2024-08-31"
          },
          attendanceRange: {
            "startDate": "2024-08-01",
            "endDate": "2024-08-31"
          },
        updateProfileDetail:{
          fullName: "Harry Brook",
          age: 25,
          gender: "male",
          email: "rewoxib649@bsomek.com",
          profilePic: "https://rb.gy/ksmsxg",
          address1: "#29 BHIVE MG Road, Mahalakshmi Chambers",
          address2: "MG Road Next to Trinity Metro Station",
          latitude:'51.507351',
          longitude:'-0.127758',
          city: "Bangalore",
          state: "Karnataka",
          country: "India",
          zipCode: "560001",
          phoneNumber: "6787986542"
        },
        AdminResetPassword: {
            email: 'john@globussoft.in',
            token: 'Gasuidaskfdiff-sdklasnlkfnldf',
            newPassword: 'newPassWord',
        },
        UpdateAdmin: {
            fullName: 'John Dev',
            age: 25,
            gender: 'male',
            profilePic: null,
            countryCode: '+91',
            address1: '#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station',
            address2: '#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station',
            city: 'Bangalore',
            state: 'Karnataka',
            country: 'India',
            zipCode: '560036',
            language: 'en',
            timezone: 'Asia/Kolkata'
        },
        AdminCreds: {
            email: 'john@globussoft.in',
            password: 'John@emp123',
        },
        UserVerification: {
            activationLink: '81bf3db0-759e-11ed-8264-5dabec18e576',
            userMail: 'jagadeeshar@globussoft.in',
            orgId: 'GLB-BAN-001',
            invitation: 1,
        },
        MailVerification: {
            activationLink: '81bf3db0-759e-11ed-8264-5dabec18e576',
            adminMail: 'john@globussoft.in',
        },
        NumberUpdation:{
            adminEmail: 'john@globussoft.in',
            newNumber: '8130418136'
        },
        PhoneVerification: {
            activationOtp: '827214',
            adminNumber: '9123456789',
        },
        AdminEmail: {
            email: 'john@globussoft.in',
        },
        AdminNumber: {
            number: '9123456789',
        },
        AdminPassword: {
            oldPassword: 'myoldPassword123',
            newPassword: 'myupdatedPassword123',
        },
        CreateUser: {
            fullName: 'Harry Brook',
            age: 25,
            gender: 'male',
            profilePic: null,
            password: 'Harry@emp123',
            countryCode: '+91',
            phoneNumber: '6787986543',
            email: 'harry@globussoft.in',
            role: '653b53bf29c9492b32097b71',
            address1: '#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station',
            address2: '#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station',
            city: 'Bangalore',
            state: 'Karnataka',
            country: 'India',
            zipCode: '560001',
            language: 'en',
            timezone: 'Asia/Kolkata'
        },
        UpdateUser: {
            fullName: 'Harry Brook',
            age: 25,
            gender: 'male',
            profilePic: null,
            address: '#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station',
            city: 'Bangalore',
            state: 'Karnataka',
            country: 'India',
            zipCode: '560001',
            language: 'en',
            timezone: 'Asia/Kolkata'
        },
        UserVerification: {
            activationLink: '81bf3db0-759e-11ed-8264-5dabec18e576',
            userMail: 'jagadeeshar@globussoft.in',
        },
        UserResetPassword: {
            email: 'jagadeeshar@globussoft.in',
            token: 'Gasuidaskfdiff-sdklasnlkfnldf',
            newPassword: 'newPassWord',
        },
        UserEmail: {
            email: 'jagadeeshar@globussoft.in',
        },
        UserPassword: {
            oldPassword: 'myoldPassword123',
            newPassword: 'myupdatedPassword123',
        },
        CreateRole:{
            role : 'member'
        },
        UpdateRole:{
            role : 'member'
        },
        UserVerification: {
            userMail: 'harry@globussoft.in',
        },
        UserPasswordSet: {
            userMail: 'harry@globussoft.in',
            oldPassword: 'Harry@emp1234',
            newPassword: 'Harry@emp123',
        },
        UserCreds: {
            userMail: 'harry@globussoft.in',
            password: 'Harry@emp123'
        },
        forgetPassword: {
            email: 'harry@globussoft.in',
        },
        resetPassword: {
            email: 'harry@globussoft.in',
            verifyToken: '81bf3db0-759e-11ed-8264-5dabec18e576',
            newPassword: 'Harry@emp123',
        },
        userPhoneVerification: {
            userNumber: '9926471094',
        },
        update2FA:{
            twoFactorAuth:0
        },
        updateProfile: {
            fullName: 'John Dev',
            age: 25,
            gender: 'male',
            profilePic: 'globus.pneg',
            address: '#29 BHIVE MG Road, Mahalakshmi Chambers,MG Road Next to Trinity Metro Station',
            language: 'en',
            timezone: 'Asia/kolkata',
            city: 'Bangalore',
            state: 'Karnataka',
            country: 'India',
            zipCode: '560036',
        },
        userAttendance: {
            date : '2023-10-16',
            latitude:  '12.971891',
            longitude:  '77.641151' 
        
        },
        CreateLeaveType: {
             name: 'casual leave',
             duration: 3,
             no_of_days: 3,
             carry_forward: 0
        },
        UpdateLeaveType: {
            name: 'casual leave',
            duration: 3,
            no_of_days: 3,
            carry_forward: 0
        },
        CreateHoliday: {
            name : 'Christmas',
            date : '2023-12-25'
        },
        UpdateHoliday: {
            name : 'Christmas',
            date : '2023-12-25'
        },
        CreateCategory: {
            categoryName: 'Sales'
        },
        UpdateCategory: {
            categoryName: 'Sales'
        },
        DeleteTask:{
            taskId:'6654f3db2c8cf8662850aef5',
        },
        filterData:{
            role:'Employee',
            location:'bangalore',
            department:'Testing',
            deleted:false
        },
        CreateTask:{
            clientId:'64df1fe3922211619a052c25',
            taskName:'Task Name',
            start_time:'2023-12-25 20:01:44',
            end_time:'2023-12-25 23:01:44',
            date:'2023-12-25',
            taskDescription: 'Test Description',
            files:[{
                url:"https://storage.googleapis.com/powerdao-dapps/fieldTracking/task/Document/Traveller_Website.docx"
            }],
            images:[{
                url:"https://storage.googleapis.com/powerdao-dapps/fieldTracking/task/Image/photo_2024-05-18_13-19-03.jpg",
                description:"Some Description about the Image."
            }],
            value:{
                currency:'IND',
                amount:20000
            },
            taskVolume:200,
            tagLogs:[{            
                tagName: 'Pending',
                time:'2024-05-29 02:27:00'
                }],
            recurrence: {
                startDate: "2024-12-26",
                endDate: "2024-12-31",
                excludedDays: [0, 6]
            }   
        },
        updateTaskStatus:{
            taskId:'6654f3db2c8cf8662850aef5',
            status:1,
            currentDateTime:'2024-05-29 02:27:00',
            latitude:  '12.971891',
            longitude:  '77.641151',
            value:{
                currency:'IND',
                amount:20000
            },
            taskVolume:200,
            tagLogs:[{            
                tagName: 'Pending',
                time:'2024-05-29 02:27:00'
                }]
        },
        UpdateTask: {
            taskId: "66570538f257c11271a1d5a3",
            clientId: "66544527c9f68b680870e8e6",
            employeeId:'32377',
            taskName: "Task Name",
            start_time: "2023-12-25 20:01:44",
            end_time: "2023-12-25 23:01:44",
            taskDescription: "Test Description",
            date: "2024-08-01",
            files: [
              {
                url: "https://storage.googleapis.com/powerdao-dapps/fieldTracking/task/Document/Traveller_Website.docx"
              }
            ],
            images: [
              {
                url: "https://storage.googleapis.com/powerdao-dapps/fieldTracking/task/Image/photo_2024-05-18_13-19-03.jpg"
               
              }
            ],
            value:{
                currency:'IND',
                amount:20000
            },
            taskVolume:200,
            tagLogs:[{            
            tagName: 'Pending',
            time:'2024-05-29 02:27:00'
            }],
            recurrence: {
                startDate: "2024-12-26",
                endDate: "2024-12-31",
                excludedDays: [0, 6]
            }  
          },
        taskIds: [
            {Id:'64df3c7c0cc4be50828ae06b'}
        ],
        AddEmpUser: {
            users: [
                {
                    "emp_id": 27564,
                    "firstName": "Deepanshu",
                    "lastName": "Test",
                    "email": "dk123win1@gmail.com",
                    "role": "Manager",
                    "department": "Default",
                    "emp_code": "PAS1188"
                  },
            ],
        },
        date:{
            "date": "2024-08-01",
          },
        empModeOfTransport:{
            currentMode:'bike',
          },
          empUpdFreqRad:{
            configRadius:10,
            configFrequency:25,
            configMode:'bike'
          },
          UpdateUserGeologsStatus:{
            emp_id: 27564,
            geologsId:["66544527c9f68b680870e8e6"]
          },
          filterReport:{
            location:'Bangalore',
            department:'Field Engineer',
            role:'manager',
            startDate:'2024-08-28',
            endDate:'2024-08-28',
            CountFilters : {
                fieldName:'pendingTaskCount',
                minValue:0,
                maxValue:10
            },
            empIds:["6654","4527"],
            exportReport:false
          },
          CreateTags:{
            tagName: "Urgent", 
            tagDescription: "Tasks that need immediate attention",
            color: "#FF5733", 
            isActive: true
        },
        updateTags:{
            tagId:'66544527c9f68b680870e8e6',
            tagName: "Urgent", 
            tagDescription: "Tasks that need immediate attention",
            color: "#FF5733", 
            isActive: true
        },
        updatedStagesOrder:{
            updatedStagesOrder: [
                { "_id": "tagId1", "order": 1 }
              ]            
        },
        deleteTags:{    
            tagIds: [
            "64f5b2d925e6f4d6d28a8e16",
            "64f5b2d925e6f4d6d28a8e17",
            "64f5b2d925e6f4d6d28a8e18"
        ]},
        deleteDocuments:{
            taskId:"64f5b2d925e6f4d6d28a8e16",
            docIds: [
                "64f5b2d925e6f4d6d28a8e16",
                "64f5b2d925e6f4d6d28a8e17",
                "64f5b2d925e6f4d6d28a8e18"
            ]
        },
        filterUserReport:{
            employee_Id:'29732',
            startDate:'2024-09-01',
            endDate:'2024-09-25',
            taskStatus:0,
            taskStage:'Pending',
            Volume:{
                minTaskVolume:0,
                maxTaskVolume:1000
            },
            Amount:{
                minAmount:0,
                maxAmount:1000
            }
        },
        reportDistanceTraveled:{
            startDate:'2024-09-01',
            endDate:'2024-09-25',
        },
        reportClientFilter:{
            taskStatus:0,
            taskStage:'Pending',
            startDate:'2024-09-01',
            endDate:'2024-09-25',
            Volume:{
                minTaskVolume:0,
                maxTaskVolume:1000
            },
            Amount:{
                minAmount:0,
                maxAmount:1000
            }
        },
        statsClientFilter:{
            employee_Id:'29732',
            taskStatus:0,
            taskStage:'Pending',
            startDate:'2024-09-01',
            endDate:'2024-09-25',
            Volume:{
                minTaskVolume:0,
                maxTaskVolume:1000
            },
            Amount:{
                minAmount:0,
                maxAmount:1000
            }
        },
        reportTaskStatus:{
            startDate:'2024-09-01',
            endDate:'2024-09-25',
        },
        reportTaskStages:{
            startDate:'2024-09-01',
            endDate:'2024-09-25',
        },
        filterTaskListDetails:{
            employee_Id:'29732'
        },
        UpdateUserBiometricConfig:{
            email:"abc@gmail.com",
            isBiometricUser:false
        },
        allEmployeesAttendance:{
         start_date: "2025-07-01",
         end_date: "2025-07-01",
         date:""  ,
         searchTerm:"virat"
        }

    }
};

const outputFile = './resources/views/swagger-api-view.json';
const endpointsFiles = ['./resources/routes/public.routes.js'];

/* NOTE: if you use the express Router, you must pass in the
   'endpointsFiles' only the root file where the route starts,
   such as: index.js, app.js, routes.js, ... */

await swagger(outputFile, endpointsFiles, doc);
