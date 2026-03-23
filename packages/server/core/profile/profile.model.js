import mongoose, { STATES } from 'mongoose';
import moment from 'moment';
import otpGenerator from 'otp-generator';

const UserSchema = new mongoose.Schema({
    fullName: { type: String, required: true },
    age: {type:Number},
    gender: {type:String},
    email: { type: String, required: true },
    password: { type: String, required: true },
    profilePic: { type: String, default: null},
    location:{type:String,default:null},
    department:{type:String ,default:null},
    status:{type:Number,default:1},//1.active,2.suspended,4.deleted.
    role: { type: String },
    emp_id : {type:String},
    orgId: { type: String, required: true },
    address1: { type: String },
    address2: { type: String },
    latitude:{ type: String, default: null},
    longitude:{ type: String, default: null},     
    city: { type: String, default: null }, 
    state: { type: String, default: null }, 
    country: { type: String, default: null },
    zipCode: { type: String, max: 6 ,default: null},
    phoneNumber: {type: String, default:null},
    timezone: { type: String },
    isSuspended:{ type: Boolean, default: false},
    forgotPasswordToken: { type: String, default: otpGenerator.generate(4, { digits:true,lowerCaseAlphabets: false, specialChars : false,upperCaseAlphabets : false }) }, //Token for handling the  forget password
    forgotTokenExpire: { type: Date, default: moment().add(1, 'day')?._d }, //Token expire for forget password
    passwordEmailSentCount: { type: Number, default: 0},
    isGeoFencingOn:{ type: Number, default: 0},
    isMobileDeviceEnabled:{ type: Number, default: 1},
    isBioMetricEnabled:{type: Number, default:0},
    isWebEnabled:{type: Number, default:0},
    frequency:{type: Number, default:20},
    geoLogsStatus:{type:Boolean,default:true},
    snap_points_limit:{type:Number,default:100},
    snap_duration_limit:{type:Number,default:60},
    isBiometricUser:{type:Boolean,default:false}
},
{ timestamps: true });
UserSchema.index({ emp_id: 1 });
UserSchema.index({ fullName: "text", email: "text", location: "text" ,department: "text",role:"text",gender: "text"});

export default mongoose.model('userSchema', UserSchema);
