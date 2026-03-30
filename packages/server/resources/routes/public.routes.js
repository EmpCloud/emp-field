import Admin from '../../core/admin/admin.routes.js';
import User from  '../../core/user/user.routes.js';
import Role from '../../core/role/role.routes.js';
import OpenUsers from '../../core/unauthorized/unauthorized.routes.js';
import Track from '../../core/tracking/track.routes.js';
import Leaves from '../../core/leave/leave.routes.js'
import Holiday from '../../core/holiday/holiday.routes.js'
import Client from '../../core/client/client.routes.js'
import Category from '../../core/category/category.routes.js';
import Task from '../../core/task/task.routes.js';
import Profile from '../../core/profile/profile.routes.js';
import Attendance from '../../core/attendance/attendance.routes.js';
import HrmsAdmin from '../../core/hrmsAdmin/hrmsAdmin.routes.js'
import transport from '../../core/transport/transport.routes.js'
import reports from '../../core/reports/reports.routes.js'
import tags from '../../core/tags/tag.routes.js'
import autoEmailReport from '../../core/autoEmailReport/autoEmailReport.routes.js'

class Routes {
    constructor(app) {
        
        app.use('/v1/admin', Admin);
        app.use('/v1/auto-Generate-Report',autoEmailReport);
        app.use('/v1/user', User);
        app.use('/v1/roles', Role);
        app.use('/v1/open-user', OpenUsers);
        app.use('/v1/client', Client);
        app.use('/v1/category',Category)
        app.use('/v1/task', Task);
        app.use('/v1/leaves',Leaves);
        app.use('/v1/holiday', Holiday);
        app.use('/v1/profile',Profile);
        app.use('/v1/profile',transport)
        app.use('/v1/attendance',Attendance);
        app.use('/v1/track',Track);
        app.use('/v1/hrmsAdmin', HrmsAdmin);
        app.use('/v1/reports', reports);
        app.use('/v1/tags', tags)

       
    }

    configureCors(app) {
        app.use((_req, res, next) => {
            res.setHeader('Access-Control-Allow-Origin', '*');
            res.setHeader('Access-Control-Allow-Methods', 'POST, PUT, DELETE, GET');
            res.setHeader('Cache-Control', 'no-cache');
            next();
        });
    }
}
export default Routes;
