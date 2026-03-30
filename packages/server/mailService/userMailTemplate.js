import sendGridMail from '@sendgrid/mail';
import config from 'config';
import { WelComeMail, verificationMail, forgetPassword,invitationRejectedMail } from './mailCode.js';

class MailService {
    async sendWelcomeMailBySendgridAPI(data) {
        sendGridMail.setApiKey(config.get('sendgrid.key'));
        const emailHtml = WelComeMail(data);
        const email = {
            from: {
                name: config.get('sendgrid.name'),
                email: config.get('sendgrid.email'),
            },
            to: data?.email,
            subject: config.get('sendgrid.WelcomeSubject'),
            html:emailHtml,
        };
        let sendStatus = await sendGridMail.send(email);
        return sendStatus;
    }
    async sendUserForgotPasswordVerificationMail(data) {
        sendGridMail.setApiKey(config.get('sendgrid.key'));
        let email = {
            from: {
                name: config.get('sendgrid.name'),
                email: config.get('sendgrid.email'),
            },
            to: data?.email,
            subject: config.get('sendgrid.resetPwdSub'),
            html: forgetPassword(data),
        };
        let sendStatus = await sendGridMail.send(email);
        return sendStatus;
    }
    async sendUserVerificationMail(data) {
        sendGridMail.setApiKey(config.get('sendgrid.key'));
        let email = {
            from: {
                name: config.get('sendgrid.name'),
                email: config.get('sendgrid.email'),
            },
            to:  data?.email,
            subject: config.get('sendgrid.generateTokenSub'),
            html: verificationMail(data),
        };
        let sendStatus = await sendGridMail.send(email);
        return sendStatus;
    }
    async sendInvitationDeclinedMail(userData,adminData) {
        sendGridMail.setApiKey(config.get('sendgrid.key'));
        let email = {
            from: {
                name: config.get('sendgrid.name'),
                email: config.get('sendgrid.email'),
            },
            to: adminData?.email,
            subject: config.get('sendgrid.InvitationRejectedSubject'),
            html: invitationRejectedMail(userData,adminData),
        };
        let sendStatus = await sendGridMail.send(email);
        return sendStatus;
    }
}

export default new MailService();
