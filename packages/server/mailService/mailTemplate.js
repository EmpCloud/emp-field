import sendGridMail from '@sendgrid/mail';
import config from 'config';
class MailTemplate {
    async sendAdminVerificationMail(mailData) {
        sendGridMail.setApiKey(config.get('sendgrid.key'));
        let bulkEmail = [];
        const mailPromise = mailData.map(data => {
            let email = {
                from: {
                    name: config.get('sendgrid.name'),
                    email: config.get('sendgrid.email'),
                },
                to: data?.email,
                subject: `EmpMonitor Admin Mail Verification`,
                html: `<!DOCTYPE html>
                <html lang="en">
                
                <head>
                    <meta charset="UTF-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <link rel="icon" href="./images/favicon.png">
                    <title>Verify Your Email Address | EmpMonitor</title>
                    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
                        rel="stylesheet">
                </head>
                
                <body style="margin:0;padding:0;max-width:600px;width:100%;margin:auto;">
                    <div style="max-width:600px;margin:0 auto;font-family: 'Poppins', sans-serif;padding:0;box-shadow: 0px 7px 51px -29px #2b478b;background-image: url('https://empmonitor.com/wp-content/uploads/2022/12/600x700.png');background-repeat: no-repeat;
                        background-size: cover;">
                        <table style="width:100%;border-spacing: 0px;text-align: center;">
                            <tbody>
                                <tr>
                                    <td style="padding: 0;">
                                        <div>
                                            <img src="https://empmonitor.com/wp-content/uploads/2022/12/User_Email_Verification_mail.png" width="100%" />
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 0 11px;">
                                        <h3 style="margin: 0;
                                        font-weight: 500;
                                        font-size: 1.2rem;
                                        color: #98187f;">Just One More Step!</h1>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 0 11px;">
                                        <p style="font-size: .9rem;
                                      margin: 6px 0 0; color: #001d63;
                                      line-height: 1.6;"> Hello ${
                                          data?.fullName
                                      }, for signing up to EmpMonitor FieldTracking Application.<br/> Please confirm your email address and start exploring our industry-leading employee monitoring solution.</p>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 0 11px;">
                                        <a href="${config.get('admin_mail_verify_link')}=${data?.emailValidateToken}&adminMail=${data?.email}" target="_blank" style=" margin: 1.3rem auto;
                                       max-width: 182px;
                                        height: 28px;
                                        line-height: 28px;
                                        background: #5e81f1;
                                        padding: 11px;
                                        text-decoration: none;
                                        color: white;
                                        display: block;
                                        border-radius: 7px;
                                        font-size: .9rem;
                                        font-weight: 500;
                                        text-transform: uppercase;
                                        letter-spacing: .1px;
                                        text-shadow: 0px 1px 10px #a5a5a5;">Verify Email Address</a>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 0 11px;">
                                        <p style="font-size: .9rem;color: #001d63;
                                        margin: 8px 0 0;">Monitor and track all your remote and on-site employees in real-time. Download <span style="display: block;
                    font-size: 1rem;
                    color: #d1452b;
                    text-transform: capitalize;">the agent on the host system and supervise the activities</span> of the user screen with the convenience of cloud data storage and robust report generation.</p>
                                    </td>
                                </tr>
                                
                                
                                <tr>
                                    <td style="padding: 0;">
                                        <ul style="display: flex;
                                        background: #1f3b7a;
                                        padding: 21px 10px 7px;
                                        border-top: 8px solid #fab141;
                                        list-style: none;
                                        margin-bottom: 0;">
                                            <li style="text-align: left;
                                            padding-top: 3rem;">
                                                <h3 style="margin: 0;
                                                color: white;
                                                font-weight: 500;
                                                font-size: 1rem;">Regards</h3>
                                                <p style="color: #ffffffdb;
                                                font-size: 0.7rem;
                                                margin: 11px 0 0;">
                                                    Team EmpMonitor
                                                </p>
                                                <a style="font-size: 0.7rem;
                                                color: #fcdd8d;
                                                text-decoration: none;" href="mailto:support@empmonitor.com">support@empmonitor.com</a>
                                                <p style="margin: 2px 0 0;font-size: 0.8rem;white-space: nowrap;">
                                                    <span style="color: #ffffffdb">Skype : </span><span><a style="font-size: 0.7rem; color: #fcdd8d;
                                      text-decoration: none;" href="skype:empmonitorsupport?chat">empmonitorsupport</a></span>
                                                </p>
                                                <ul class="social-icons" style="margin-top:7px;padding-left: 0;
                                                display: flex;
                                                justify-content: flex-start;
                                                align-items: center;
                                                flex-wrap: wrap;
                                                list-style: none;
                                                gap: 3px;
                                              ">
                                                    <li style="    background: #2049a7;
                                                    border-radius: 50%;
                                                    width: 28px;
                                                    height: 28px;
                                                    text-align: center;
                                                    box-shadow: 0px 5px 9px -4px black, inset 0px 4px 3px -4px white, 0px 2px 16px -7px black;
                                                    cursor: pointer;
                                                    border: 2px solid #1f3b7a;
                                                    margin:0;
                                                ">
                                                        <a href="https://www.facebook.com/EmpMonitor/" target="_blank">
                                                            <img src="https://empmonitor.com/wp-content/uploads/2022/12/facebook-3.png" style="
                                                      max-width: 15px;
                                                      max-height: 15px;
                                                      height: 100%;
                                                      width: 100%;    padding-top: 6px;
                                                    " />
                                                        </a>
                                                    </li>
                                                    <li style="    background: #2049a7;
                                                    border-radius: 50%;
                                                    width: 28px;
                                                    height: 28px;
                                                    box-shadow: 0px 5px 9px -4px black, inset 0px 4px 3px -4px white, 0px 2px 16px -7px black;
                                                    cursor: pointer; margin:0;
                                                    border: 2px solid #1f3b7a;    text-align: center;
                                                ">
                                                        <a href="https://twitter.com/empmonitor" target="_blank">
                                                            <img src="https://empmonitor.com/wp-content/uploads/2022/12/twitter.png" style="
                                                      max-width: 15px;
                                                      max-height: 15px;
                                                      height: 100%;
                                                      width: 100%;    padding-top: 6px;
                                                    " />
                                                        </a>
                                                    </li>
                                                    <li style="    background: #2049a7;
                                                    border-radius: 50%;
                                                    width: 28px;
                                                    height: 28px;
                                                    box-shadow: 0px 5px 9px -4px black, inset 0px 4px 3px -4px white, 0px 2px 16px -7px black;
                                                    cursor: pointer; margin:0;
                                                    border: 2px solid #1f3b7a;    text-align: center;
                                                ">
                                                        <a href="https://in.linkedin.com/company/empmonitor?original_referer=https%3A%2F%2Fempmonitor.com%2F"
                                                            target="_blank">
                                                            <img src="https://empmonitor.com/wp-content/uploads/2022/12/linkedin.png" style="
                                                      max-width: 15px;
                                                      max-height: 15px;
                                                      height: 100%;
                                                      width: 100%;
                                                      object-fit: cover;padding-top: 6px;
                                                    " />
                                                        </a>
                                                    </li>
                                                    <li style="    background: #2049a7;
                                                    border-radius: 50%;
                                                    width: 28px;
                                                    height: 28px;
                                                    box-shadow: 0px 5px 9px -4px black, inset 0px 4px 3px -4px white, 0px 2px 16px -7px black;
                                                    cursor: pointer; margin:0;
                                                    border: 2px solid #1f3b7a;    text-align: center;
                                                ">
                                                        <a href="https://www.youtube.com/channel/UCh2X5vn5KBkN-pGY5PxJzQw"
                                                            target="_blank">
                                                            <img src="https://empmonitor.com/wp-content/uploads/2022/12/play-2.png" style="
                                                      max-width: 15px;
                                                      max-height: 15px;
                                                      height: 100%;
                                                      width: 100%;
                                                      object-fit: cover;
                                                      padding-top: 6px;
                                                    " />
                                                        </a>
                                                    </li>
                                                </ul>
                                            </li>
                                            <li style="text-align: right;">
                                                <a style="
                                                  font-size: 0.8rem;
                                                  color: #fcdd8d;
                                                  text-decoration: none;
                                                  cursor: pointer;
                                                "><img style="max-width: 101px; border-radius: 4px" src="https://empmonitor.com/wp-content/uploads/2022/12/qr-code-2.png" /></a>
                                                <h3 style="margin: 0;color: white;font-weight: 500;font-size: 1rem;">Connect with us!
                                                </h3>
                                                <p style="color: #ffffffdb;
                                                font-size: 0.7rem;
                                                margin: 5px 0;
                                                ">
                                                    Scan this QR code and talk to our experts.
                                                </p>
                
                                                <p style="margin: 6px 0 0; font-size: 0.6rem;color: white">
                                                    In case of queries, raise a ticket via the
                                                    <a href="https://help.empmonitor.com/support/solutions" target="_blank"
                                                        style="font-size: 0.6rem; color: #fcdd8d">Help Desk</a>
                                                    of EmpMonitor and our team will get back to you shortly
                                                    after.
                                                </p>
                                            </li>
                                        </ul>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding: 0 11px;
                                    background: #1f3b7a;
                                    border-top: 1px solid #ffffff1a;">
                                        <p style="font-size: .7rem;
                                        margin: 0;
                                        color: white;
                                        padding: 11px;
                                        font-weight: 500;">Copyright @2020 - 2022 EmpMonitor | All Rights Reserved.</p>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </body>
                
                </html>`,
            };
            return email;
        });
        bulkEmail = await Promise.all(mailPromise);
        let sendStatus = await sendGridMail.send(bulkEmail);
        return sendStatus;
    }
    async sendWelcomeMailAdminBySendgridAPI(data) {
        sendGridMail.setApiKey(config.get('sendgrid.key'));
        var userName=""                                                                                                                                                                                                                         ;
        if(data.username){
            userName = `<tr>
            <th style="padding: 0.6rem;
            border-right: 1px solid #f0f0f0;
            color: #0e37bc;
            font-weight: 600;
            font-size: .9rem;
            ">
                Password
            </th>
            <td style="padding: 0.6rem;
            min-width: 158px;
            color: #000000;
            font-size: .9rem;">
                ${data.username}
            </td>
        </tr>`;
        }
        const email = {
            from: {
                name: config.get('sendgrid.name'),
                email: config.get('sendgrid.email'),
            },
            to: data?.email,
            subject: `Welcome to EmpMonitor Field Tracking`,
            html: `<!DOCTYPE html>
            <html lang="en">
            
            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="icon" href="./images/favicon.png">
                <title>Account Activated | See you in the EmpMonitor's dashboard!</title>
                <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
            </head>
            
            <body style="margin:0;padding:0;max-width:600px;width:100%;margin:auto;">
                <div style="max-width:600px;margin:0 auto;font-family: 'Poppins', sans-serif;padding:0;box-shadow: 0px 7px 51px -29px #2b478b;background-image: url('https://empmonitor.com/wp-content/uploads/2022/12/600x700.png');background-repeat: no-repeat;
                    background-size: cover;">
                    <table style="width:100%;border-spacing: 0px;text-align: center;">
                        <tbody>
                            <tr>
                                <td style="padding: 0;">
                                    <div>
                                        <img src="https://empmonitor.com/wp-content/uploads/2022/12/User_Welcome_Email-1.png"
                                            width="100%" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0 11px;">
                                    <h3 style="margin: 0;
                                    font-weight: 500;
                                    font-size: 1.1rem;">Hi, ${data?.fullName}</h1>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0 11px;">
                                    <p style="font-size: .9rem;
                                  margin: 6px 0 0; color: #001d63;
                                  line-height: 1.6;"><span
                                            style="font-size: 1.3rem;color: #5e5dda;text-transform: uppercase;">Welcome</span><br />
                                        on board
                                        to the family of EmpMonitor as role <b>Admin</b> </p>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0 11px;">
                                    <p style="font-size: .9rem;color: #001d63;
                                    margin: 8px 0 0;">We are super excited to share the features we’ve got for you. Keep an extra
                                        eye on your administration through clever insights on the employee productivity matrix and
                                        user-friendly dashboard.</p>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div style="margin: 1.1rem 0">
                                        <table style=" margin: 2rem auto;
                                        border: 1px solid #e5ebff;
                                        border-collapse: collapse;
                                        box-shadow: 0px 3px 7px -8px;
                                        background: #e4ebff40;
                                              ">
                                            <tbody>
                                                <tr>
                                                    <th style="padding: 0.6rem;
                                                    border-right: 1px solid #f0f0f0;
                                                    color: #0e37bc;
                                                    font-weight: 600;
                                                    font-size: .9rem;
                                                    ">
                                                        Email Id
                                                    </th>
                                                    <td style="padding: 0.6rem;
                                                    min-width: 158px;
                                                    color: #000000;
                                                    font-size: .9rem;">
                                                        <a href="#0" style="    text-decoration: none;
                                                        user-select: none;
                                                        cursor: default;
                                                        color: black;">${data?.email}</a>
                                                    </td>
                                                </tr>
                                                ${userName}
                                            </tbody>
                                        </table>
                                    </div>
            
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0 11px;">
                                    <p style="font-size: .9rem;color: #001d63;
                                    margin: 8px 0 0;">We are confident that it's going to be an awesome journey for both of us!</p>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0 11px;">
                                    <a href="${config.get('admin_sign_in_link')}" target="_blank" style=" margin: 1.3rem auto;
                                    max-width: 148px;
                                    height: 28px;
                                    line-height: 28px;
                                    background: #5e81f1;
                                    padding: 11px;
                                    text-decoration: none;
                                    color: white;
                                    display: block;
                                    border-radius: 7px;
                                    font-size: .9rem;
                                    font-weight: 500;
                                    text-transform: uppercase;
                                    letter-spacing: .1px;
                                    text-shadow: 0px 1px 10px #a5a5a5;">Get Started</a>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0 11px;">
                                    <a style="font-size: 1.2rem;
                                    font-weight: 600;
                                    margin: 0.2rem auto 0;
                                    display: block;
                                    color: #f02e2e;
                                    text-transform: uppercase;" href="https://app.empmonitor.com/login"
                                        target="_blank">Click here</a>
                                    <p style="font-size: .9rem;color: #001d63;margin: 8px 0 16px;">in case you are facing any difficulties in logging in.<br />
                                    </p>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0 11px;">
                                    <ul style="    padding: 0;
                                   list-style: none;
                                  
                                   margin: 0 auto 1rem;
                                   gap: 12px;">
                                        <li style=" display: inline-block;cursor: pointer;">
                                            <button type="button" style="text-align: center;
                                           padding: 8px;
                                           display: flex;
                                           justify-content: center;
                                           align-items: center;
                                           background: #ffcb7b;
                                           outline: 0;
                                           border: 1px solid white;
                                           border-radius: 6px;    cursor: pointer;">
                                                <div><img style="width: 40px;
                height: 31px;
                object-fit: cover;" src="https://empmonitor.com/wp-content/uploads/2022/12/computer.png" /></div>
                                                <p style="color: black;
                                                margin-left: 7px;">Download Installation Brochure</p>
                                            </button>
            
                                        </li>
                                        <li style="display: inline-block;cursor: pointer;">
                                            <button type="button" style=" text-align: center;
                                        padding: 8px;
                                        display: flex;
                                        justify-content: center;
                                        align-items: center;
                                        background: #ffcb7b;
                                        outline: 0;
                                        border: 1px solid white;
                                        border-radius: 6px;    cursor: pointer;">
                                                <div><img style="    width: 40px;
                height: 31px;
                object-fit: cover;" src="https://empmonitor.com/wp-content/uploads/2022/12/facebook-7.png" /></div>
                                                <p style=" color: black;
                                                margin-left: 7px;">Watch Onboarding Video</p>
                                            </button>
                                        </li>
                                    </ul>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0;">
                                    <ul style="display: flex;
                                    background: #1f3b7a;
                                    padding: 21px 10px 7px;
                                    border-top: 8px solid #fab141;
                                    list-style: none;
                                    margin-bottom: 0;">
                                        <li style="text-align: left;
                                        padding-top: 3rem;">
                                            <h3 style="margin: 0;
                                            color: white;
                                            font-weight: 500;
                                            font-size: 1rem;">Regards</h3>
                                            <p style="color: #ffffffdb;
                                            font-size: 0.7rem;
                                            margin: 11px 0 0;">
                                                Team EmpMonitor
                                            </p>
                                            <a style="font-size: 0.7rem;
                                            color: #fcdd8d;
                                            text-decoration: none;" href="mailto:support@empmonitor.com">support@empmonitor.com</a>
                                            <p style="margin: 2px 0 0;font-size: 0.8rem;white-space: nowrap;">
                                                <span style="color: #ffffffdb">Skype : </span><span><a style="font-size: 0.7rem; color: #fcdd8d;
                                  text-decoration: none;" href="skype:empmonitorsupport?chat">empmonitorsupport</a></span>
                                            </p>
                                            <ul class="social-icons" style="margin-top:7px;padding-left: 0;
                                            display: flex;
                                            justify-content: flex-start;
                                            align-items: center;
                                            flex-wrap: wrap;
                                            list-style: none;
                                            gap: 3px;
                                          ">
                                                <li style="    background: #2049a7;
                                                border-radius: 50%;
                                                width: 28px;
                                                height: 28px;
                                                text-align: center;
                                                box-shadow: 0px 5px 9px -4px black, inset 0px 4px 3px -4px white, 0px 2px 16px -7px black;
                                                cursor: pointer;
                                                border: 2px solid #1f3b7a;
                                                margin:0;
                                            ">
                                                    <a href="https://www.facebook.com/EmpMonitor/" target="_blank">
                                                        <img src="https://empmonitor.com/wp-content/uploads/2022/12/facebook-3.png"
                                                            style="
                                                  max-width: 15px;
                                                  max-height: 15px;
                                                  height: 100%;
                                                  width: 100%;    padding-top: 6px;
                                                " />
                                                    </a>
                                                </li>
                                                <li style="    background: #2049a7;
                                                border-radius: 50%;
                                                width: 28px;
                                                height: 28px;
                                                box-shadow: 0px 5px 9px -4px black, inset 0px 4px 3px -4px white, 0px 2px 16px -7px black;
                                                cursor: pointer; margin:0;
                                                border: 2px solid #1f3b7a;    text-align: center;
                                            ">
                                                    <a href="https://twitter.com/empmonitor" target="_blank">
                                                        <img src="https://empmonitor.com/wp-content/uploads/2022/12/twitter.png"
                                                            style="
                                                  max-width: 15px;
                                                  max-height: 15px;
                                                  height: 100%;
                                                  width: 100%;    padding-top: 6px;
                                                " />
                                                    </a>
                                                </li>
                                                <li style="    background: #2049a7;
                                                border-radius: 50%;
                                                width: 28px;
                                                height: 28px;
                                                box-shadow: 0px 5px 9px -4px black, inset 0px 4px 3px -4px white, 0px 2px 16px -7px black;
                                                cursor: pointer; margin:0;
                                                border: 2px solid #1f3b7a;    text-align: center;
                                            ">
                                                    <a href="https://in.linkedin.com/company/empmonitor?original_referer=https%3A%2F%2Fempmonitor.com%2F"
                                                        target="_blank">
                                                        <img src="https://empmonitor.com/wp-content/uploads/2022/12/linkedin.png"
                                                            style="
                                                  max-width: 15px;
                                                  max-height: 15px;
                                                  height: 100%;
                                                  width: 100%;
                                                  object-fit: cover;padding-top: 6px;
                                                " />
                                                    </a>
                                                </li>
                                                <li style="    background: #2049a7;
                                                border-radius: 50%;
                                                width: 28px;
                                                height: 28px;
                                                box-shadow: 0px 5px 9px -4px black, inset 0px 4px 3px -4px white, 0px 2px 16px -7px black;
                                                cursor: pointer; margin:0;
                                                border: 2px solid #1f3b7a;    text-align: center;
                                            ">
                                                    <a href="https://www.youtube.com/channel/UCh2X5vn5KBkN-pGY5PxJzQw"
                                                        target="_blank">
                                                        <img src="https://empmonitor.com/wp-content/uploads/2022/12/play-2.png"
                                                            style="
                                                  max-width: 15px;
                                                  max-height: 15px;
                                                  height: 100%;
                                                  width: 100%;
                                                  object-fit: cover;
                                                  padding-top: 6px;
                                                " />
                                                    </a>
                                                </li>
                                            </ul>
                                        </li>
                                        <li style="text-align: right;">
                                            <a style="
                                              font-size: 0.8rem;
                                              color: #fcdd8d;
                                              text-decoration: none;
                                              cursor: pointer;
                                            "><img style="max-width: 101px; border-radius: 4px"
                                                    src="https://empmonitor.com/wp-content/uploads/2022/12/qr-code-2.png" /></a>
                                            <h3 style="margin: 0;color: white;font-weight: 500;font-size: 1rem;">Connect with us!
                                            </h3>
                                            <p style="color: #ffffffdb;
                                            font-size: 0.7rem;
                                            margin: 5px 0;
                                            ">
                                                Scan this QR code and talk to our experts.
                                            </p>
            
                                            <p style="margin: 6px 0 0; font-size: 0.6rem;color: white">
                                                In case of queries, raise a ticket via the
                                                <a href="https://help.empmonitor.com/support/solutions" target="_blank"
                                                    style="font-size: 0.6rem; color: #fcdd8d">Help Desk</a>
                                                of EmpMonitor and our team will get back to you shortly
                                                after.
                                            </p>
                                        </li>
                                    </ul>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding: 0 11px;
                                background: #1f3b7a;
                                border-top: 1px solid #ffffff1a;">
                                    <p style="font-size: .7rem;
                                    margin: 0;
                                    color: white;
                                    padding: 11px;
                                    font-weight: 500;">Copyright @2020 - 2022 EmpMonitor | All Rights Reserved.</p>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </body>
            
            </html>`,
        };
        let sendStatus = await sendGridMail.send(email);
        return sendStatus;
    }
    async sendAdminForgotPasswordVerificationMail(data) {
        sendGridMail.setApiKey(config.get('sendgrid.key'));
        let bulkEmail = [];
        // const mailPromise = mailData.map(data => {
            let email = {
                from: {
                    name: config.get('sendgrid.name'),
                    email: config.get('sendgrid.email'),
                },
                to: data?.email,
                subject: `EmpMonitor Forget Password Request`,
                html: `<!DOCTYPE html>

                <html xmlns="http://www.w3.org/1999/xhtml">
                  <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                    <meta
                      name="viewport"
                      content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0"
                    />
                
                    <link
                      rel="stylesheet"
                      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"
                      integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw=="
                      crossorigin="anonymous"
                      referrerpolicy="no-referrer"
                    />
                    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
                    <link
                      href="https://fonts.googleapis.com/css2?family=Comfortaa&display=swap"
                      rel="stylesheet"
                    />
                
                    <link
                      href="https://fonts.googleapis.com/css2?family=Roboto&display=swap"
                      rel="stylesheet"
                    />
                
                    <link
                      rel="icon"
                      href="https://empmonitor.com/wp-content/uploads/2019/03/empmonitor_icon.png"
                    />
                    <link
                      href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap"
                      rel="stylesheet"
                    />
                    <title>Emp Monitor : Forgot Password</title>
                  </head>
                
                  <body style="background: #fff; margin: 0">
                    <div
                      style="
                        /* background-image: url('power_email_temp_bg.png');
                    background-size: cover;
                    background-repeat: no-repeat; */
                        max-width: 650px;
                        margin: auto;
                        box-shadow: 0px 6px 15px #e9effc;
                        border-radius: 40px;
                        font-family: 'Montserrat', sans-serif;
                      "
                    >
                      <table
                        style="
                          margin: auto;
                          width: 100%;
                          font-size: 14px;
                          border-spacing: 0px;
                          border-radius: 0px 18px 0px 0px;
                          background: #fff;
                          margin-bottom: 18px;
                        "
                        cellspacing="0"
                        cellpadding="0"
                        border="0"
                        ;
                      >
                        <tbody
                          style="
                            background: url('https://empmonitor.com/wp-content/uploads/2024/06/bg-forgot_password.webp');
                            background-size: cover;
                            background-repeat: no-repeat;
                            background-position: center;
                          "
                        >
                          <tr>
                            <td style="padding: 0; text-align: center">
                              <img
                                src="https://empmonitor.com/wp-content/uploads/2024/06/site-logo-white_1.webp"
                                alt=""
                                style="margin-bottom: 30px; margin-top: 30px"
                              />
                            </td>
                          </tr>
                
                          <tr>
                            <td>
                              <div
                                style="
                                  padding: 30px 25px;
                                  margin-bottom: 25px;
                                  background: url('https://empmonitor.com/wp-content/uploads/2024/06/card.webp');
                                  background-size: cover;
                                  background-repeat: no-repeat;
                                  background-position: center;
                                  border-radius: 18px;
                                  margin: 0 33px;
                                  border: 0;
                                  border: 1px solid #f3f3f3;
                                  color: #fff;
                                "
                              >
                                <h2
                                  style="
                                    text-align: center;
                                    font-size: 30px;
                                    margin-bottom: 0;
                                    font-weight: 500;
                                  "
                                >
                                  Forgot Password
                                </h2>
                
                                <div style="max-width: 150px; margin: auto; margin-block: 10px">
                                  <img
                                    src="https://empmonitor.com/wp-content/uploads/2024/06/1_3.webp"
                                    style="width: 100%; margin: auto"
                                    alt=""
                                  />
                                </div>
                
                                <p
                                  style="
                                    text-align: center;
                                    font-size: 18px;
                                    margin-top: 0;
                                    margin-bottom: 2rem;
                                  "
                                >
                                  Reset Your EmpMonitor Password
                                </p>
                                <p style="font-size: 18px">Hi ${data?.fullName?data?.fullName:"[Recipient's Name]"}</p>
                                <p
                                  style="font-size: 18px; line-height: 1.5; text-align: justify"
                                >
                                  Are you having trouble remembering your password? Don’t worry,
                                  it happens to everyone.
                                </p>
                
                                <p
                                  style="
                                    margin-top: 0;
                                    font-size: 18px;
                                    line-height: 1.5;
                                    text-align: justify;
                                  "
                                >
                                  To proceed, please click the link below:
                                </p>
                
                                <p style="text-align: center; margin-block: 2rem">
                                  <a
                                    style="
                                      cursor: pointer;
                                      text-decoration: none;
                                      padding: 10px 50px;
                                      border-radius: 8px;
                                      font-size: 18px;
                                      font-weight: 500;
                                      color: #fff;
                                      background: #37b7ff;
                                    "
                                    href="${config.get('user_mail_forgotPassword_verify_link')}=${data?.forgotPasswordToken}&userMail=${
                                        data?.email
                                    }" target="_blank"
                                    >Reset Password Link</a
                                  >
                                </p>
                                <p style="font-size: 18px">
                                  If you did not request a password reset, please disregard this
                                  email. Your account remains secure.
                                </p>
                                <p style="font-size: 18px; margin-bottom: 2rem">
                                  For further assistance, feel free to contact our support team.
                                </p>
                
                                <!-- <a href="#" style="text-decoration: none; font-weight: bold; color: black;font-size: 18px;">Click to Explore</a> -->
                
                                <p style="font-size: 16px; margin-bottom: 0">Regards</p>
                                <p style="font-size: 18px; margin-top: 10px; font-weight: 600">
                                  EmpMonitor Team
                                </p>
                              </div>
                            </td>
                          </tr>
                
                          <tr>
                            <td>
                              <div style="width: 100%; margin: auto" class="media-footer">
                                <div
                                  style="
                                    /* display: flex; */
                                    /* justify-content: space-between; */
                                    align-content: center;
                                    gap: 30px;
                                    margin: auto;
                                    /* background: #fff; */
                                    padding: 15px 0;
                                    /* border-top: 1px solid #bfbfbf; */
                                    width: 100%;
                                    margin-top: 18px;
                                  "
                                >
                                  <table style="margin: auto">
                                    <tbody>
                                      <tr>
                                        <td>
                                          <div style="margin: 10px">
                                            <a
                                              href="https://www.facebook.com/EmpMonitor/"
                                              target="_blank"
                                              ><img
                                                src="https://empmonitor.com/wp-content/uploads/2020/01/f_icon.png"
                                                style="width: 30px"
                                            /></a>
                                          </div>
                                        </td>
                
                                        <td>
                                          <div style="margin: 10px">
                                            <a
                                              href="https://www.linkedin.com/company/empmonitor/"
                                              target="_blank"
                                              ><img
                                                src="https://empmonitor.com/wp-content/uploads/2020/04/linkedin.png"
                                                style="width: 30px"
                                            /></a>
                                          </div>
                                        </td>
                                        <td>
                                          <div style="margin: 10px">
                                            <a
                                              href="https://www.youtube.com/channel/UCh2X5vn5KBkN-pGY5PxJzQw"
                                              target="_blank"
                                              ><img
                                                src="https://empmonitor.com/wp-content/uploads/2020/04/youtube.png"
                                                style="width: 30px"
                                            /></a>
                                          </div>
                                        </td>
                                        <td>
                                          <div style="margin: 10px">
                                            <a
                                              href="https://twitter.com/empmonitor"
                                              target="_blank"
                                              ><img
                                                src="https://empmonitor.com/wp-content/uploads/2020/01/t_icon.png"
                                                style="width: 30px"
                                            /></a>
                                          </div>
                                        </td>
                                        <td>
                                          <div style="margin: 10px">
                                            <a href="skype:empmonitorsupport" target="_blank"
                                              ><img
                                                src="https://empmonitor.com/wp-content/uploads/2022/11/skype_icon.png"
                                                style="width: 30px"
                                            /></a>
                                          </div>
                                        </td>
                                      </tr>
                                    </tbody>
                                  </table>
                                </div>
                              </div>
                            </td>
                          </tr>
                          <tr>
                            <td width="100%" height="20">
                              <p
                                style="
                                  margin: 0;
                                  font-size: 16px;
                                  /* color: #000; */
                                  font-family: 'comfortaa', sans-serif;
                                  line-height: 18px;
                                  text-align: center;
                                  /* background: #000000c7; */
                                  border-radius: 0px 0px 0px 0px;
                                  margin-bottom: 2rem;
                                  color: #ffffff;
                                "
                              >
                                Copyright 2024 @ EmpMonitor | All Rights Reserved
                              </p>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                
                      <!-- 
                        <table style="
                          margin: auto;
                          width: 100%;
                          font-size: 14px;
                          border-spacing: 0px;
                          border-radius: 0px 18px 0px 0px;
                          /* background: #fff; */
                        " cellspacing="0" cellpadding="0" border="0" ;>
                            <tbody> -->
                
                      <!-- </tbody>
                        </table> -->
                    </div>
                  </body>
                </html>
                `,
            };
            // return email;
        // });
        // bulkEmail = await Promise.all(mailPromise);
        let sendStatus = await sendGridMail.send(email);
        return sendStatus;
    }
}
export default new MailTemplate();
