import mongoose, { STATES } from 'mongoose';
import moment from 'moment';
import otpGenerator from 'otp-generator';

const adminSchema = new mongoose.Schema({
    fullName: { type: String, default: null ,required:true},
    age: {type:Number},
    gender: {type:String,required:true},
    email: { type: String, required: true },
    password: { type: String, required: true },
    profilePic: { type: String, default: null},
    role: { type: String ,default:null},
    emp_id : {type:String},
    orgId: { type: String, default: null ,required:true},
    address1: { type: String },
    address2: { type: String },
    latitude:{ type: String, default: null},
    longitude:{ type: String, default: null},     
    city: { type: String, default: null ,required:true}, 
    state: { type: String, default: null ,required:true}, 
    country: { type: String, default: null ,required:true},
    zipCode: { type: String, max: 6 },
    phoneNumber: {type: Number,default:null,required:true},
    timezone: { type: String ,default:null,required:true},
    isSuspended:{ type: Boolean, default: false},
    orgRadius:{ type: Number, default: 10},
    snap_points_limit: { type: Number, default: 100},//points
    snap_duration_limit: { type: Number, default: 60},//mins
    forgotPasswordToken: { type: String, default: otpGenerator.generate(4, { digits:true,lowerCaseAlphabets: false, specialChars : false,upperCaseAlphabets : false }) }, //Token for handling the  forget password
    forgotTokenExpire: { type: Date, default: moment().add(1, 'day')?._d }, //Token expire for forget password
    passwordEmailSentCount: { type: Number, default: 0},
},
{ timestamps: true });


export default mongoose.model('adminSchema', adminSchema);

