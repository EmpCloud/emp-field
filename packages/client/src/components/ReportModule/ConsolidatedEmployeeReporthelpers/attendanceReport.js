import ChartProvider from 'components/ChartContext/Provider';
import jsPDF from 'jspdf';
import { useContext } from 'react';
import * as XLSX from 'xlsx';
import moment from 'moment';

export const exportPDFattendanceReport = (
  employeeReportData,
  employeeStatsData,
  attendanceTable,
  attendanceVennData,
  profileImg,
  modeBike,
  taskListData
) => {
  const doc = new jsPDF('portrait', 'pt', 'a4');

  const pageWidth = doc.internal.pageSize.getWidth();
  const pageHeight = doc.internal.pageSize.getHeight();
  let emplAttendanceTableHeaders = [
    'Date',
    'Status',
    'Check In',
    'Check Out',
    'Request Status',
    'Overridden By',
    'Total',
  ];
  // Add image as background
  const imgData =
    'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/header-2.png?timestamp=1726132224089'; // Image URL
  const imgData02 =
    'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/header-1.png?timestamp=1726132199725';
  // const imgData = 'https://i.ibb.co/987d67P/banner-902589-640.webp'; // Image URL
  const imgWidth = pageWidth; // Set width of the image to match the page width
  const imgHeight = 100; // Height of the image
  const imgData03 =
    'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/EmpMonitor-logo-tag1-white.png?timestamp=1726140771727';
  const imgHeight03 = 25;
  const imgtaskstatusheight = 225;
  const imgWidth03 = pageWidth / 6;

  //   const charttasksStatus = taskStatusImg;
  //   // const charttasksStage= taskStageImg;
  //   const charttaskStages = taskStageImg;

  try {
    // Add image to the top of the page
    doc.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight);
    doc.addImage(imgData03, 'PNG', 20, 44, imgWidth03, imgHeight03);
    // Add Title
    doc.setFontSize(19);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.setTextColor(31, 58, 120);
    doc.text('Attendance REPORT', pageWidth / 2, imgHeight + 30, {
      align: 'center',
    });

    // Add Date
    const dateGet = new Date();
    const dateYear = dateGet.getFullYear();
    const dateMonth = dateGet.getMonth();
    const dateDate = dateGet.getDate();
    const dateText = `Date : ${dateDate}-${dateMonth}-${dateYear}`;
    doc.setFontSize(11);
    doc.setFont('Quicksand-Medium', 'normal');

    const textWidth = doc.getTextWidth(dateText);
    doc.text(dateText, pageWidth - textWidth - 20, imgHeight + 30);
    doc.setTextColor(31, 58, 120);

    // Add the image inside the circular area (make it fit within the circle)
    if (profileImg) {
      doc.addImage(profileImg, 'PNG', 20, 155, 46, 46);
    } else {
      // doc.circle()
      doc.setFillColor(255, 0, 0); // Red fill
      // Set the stroke (outline) color

      doc.circle(45, 180, 23, 'FD');
    }

    const empId = `Employee Id - ${employeeReportData.emp_id}`;
    doc.setFontSize(9);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(empId, 82, 170);
    const empName = `${employeeReportData.fullName}`;
    doc.setFontSize(17);
    doc.setFont('Quicksand-SemiBold', 'normal');
    doc.text(empName, 82, 185);
    const empMail = `${employeeReportData?.role || 'Developer'} - ${employeeReportData.email}`;
    doc.setFontSize(11);
    doc.setFont('Quicksand-SemiBold', 'normal');
    doc.text(empMail, 82, 197);

    // doc.addImage(profileImg,'PNG',20,imgHeight+50,20,20)
    doc.setFontSize(14);
    doc.setFont('Quicksand-Medium', 'normal');
    const filterText = 'Applied Filters';
    //   const filterText =
    //     'Filters: Department - Sales, Location - New York, Date - August 2024';

    let currentY = 230; // Initial y position for the first filter

    const padding = 10; // Padding for the text
    const fontSize = 14; // Font size
    const rectHeight = 65; // Height of each rectangle
    let currentX = 10; // Initial x position
    const chartsmallcard = [
      { title: 'Tasks', col: '#78A3FF' },
      { title: 'Clients', col: '#FFAC64' },
      { title: 'Distance Travelled', col: '#FFA5C9' },
      { title: 'Net Task Value', col: '#1AB6AE' },
      { title: 'Net Task Volume', col: '#6E67CA' },
      { title: 'Mode of Transport', col: '#63D29B' },
    ];

    for (let i = 0; i < chartsmallcard.length; i++) {
      const filterText01 = chartsmallcard[i].title;

      doc.setFontSize(8);
      doc.setFont('Quicksand-Medium', 'normal');

      const textWidth01 = doc.getTextWidth(filterText01);
      const rectWidth = (pageWidth - 70) / 6;

      doc.setFillColor(chartsmallcard[i].col);
      const borderRadius = 5; // Define your border radius
      doc.roundedRect(
        currentX,
        currentY - padding - 5,
        rectWidth,
        rectHeight,
        borderRadius,
        borderRadius,
        'F'
      );

      // Set text color
      doc.setTextColor(255, 255, 255);

      const textx01 = currentX + rectWidth / 2;
      const texty01 = currentY + rectHeight / 2 - padding - 5;
      if (i == 0) {
        doc.setFontSize(18);
        doc.setFont('Quicksand-Bold', 'normal');
        const taskStatsData = `${taskListData?.length || 0}`;
        doc.text(taskStatsData, 54, 242, {
          align: 'center',
        });
      }
      if (i == 1) {
        doc.setFontSize(18);
        doc.setFont('Quicksand-Bold', 'normal');
        const taskStatsData = `${employeeStatsData?.clientCounts}`;
        const xcountscod = textx01 - 7;
        doc.text(taskStatsData, 151, 242, {
          align: 'center',
        });
      }
      if (i == 2) {
        doc.setFontSize(18);
        doc.setFont('Quicksand-Bold', 'normal');
        const taskStatsData = `${employeeStatsData?.distanceTraveled.toFixed(2) || 0}`;
        const taskStatsDataTWid = doc.getTextWidth(taskStatsData);
        const xcountscod = textx01 - 30;
        doc.text(taskStatsData, 249, 242, {
          align: 'center',
        });
        doc.setFontSize(6);
        doc.setFont('Quicksand-Medium', 'normal');
        const kmxw = 249 + taskStatsDataTWid / 2;

        doc.text('KM', kmxw, 242);
      }
      if (i == 3) {
        doc.setFontSize(18);
        doc.setFont('Quicksand-Bold', 'normal');
        // taskValue
        const taskStatsData =
          employeeStatsData?.taskValue > 999
            ? `${Math.round(employeeStatsData.taskValue / 1000)}k`
            : (employeeStatsData?.taskValue || 0).toFixed(2);
        const taskStatsDataTWid = doc.getTextWidth(taskStatsData);
        const xcountscod = textx01 - 30;
        doc.text(taskStatsData, 346, 242, {
          align: 'center',
        });
        doc.setFontSize(10);
        doc.setFont('Quicksand-Medium', 'normal');
      }
      if (i == 4) {
        doc.setFontSize(18);
        doc.setFont('Quicksand-Bold', 'normal');
        const taskStatsData =
          employeeStatsData?.netTaskVolume > 999
            ? `${Math.round(employeeStatsData.netTaskVolume / 1000)}k`
            : (employeeStatsData?.netTaskVolume || 0).toFixed(2);
        const xcountscod = textx01 - 17;
        doc.text(taskStatsData, 444, 242, {
          align: 'center',
        });
      }
      if (i == 5) {
        doc.setFontSize(18);
        doc.setFont('Quicksand-Bold', 'normal');
        const taskStatsData = `${employeeStatsData?.netTaskVolume}`;
        const xcountscod = textx01 - 17;

        doc.addImage(modeBike, 'PNG', xcountscod, 222, 30, 30);
      }
      doc.setFontSize(9);
      doc.setFont('Quicksand-Medium', 'normal');
      doc.text(filterText01, textx01, 258, {
        align: 'center',
        baseline: 'middle',
      });

      currentX += rectWidth + 10;
    }

    let taskXcord = 10;
    const attendanceChartHeadYcord = 290;
    const attendanceChartHeadWd = pageWidth - 20;
    // for(let i=0;i<charttasks.length;i++){

    doc.setFillColor(164, 92, 236);
    const borderRadius = 3; // Define your border radius
    doc.roundedRect(
      10,
      attendanceChartHeadYcord,
      attendanceChartHeadWd,
      30,
      borderRadius,
      borderRadius,
      'F'
    );
    const taskStatustext = 'Attendance';
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(10);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.text(taskStatustext, 20, 308);

    doc.setFillColor(151, 190, 42); // Red fill
    doc.circle(pageWidth / 2 - 50, 425, 96, 'F');
    const attenDancePresentCntt = attendanceVennData[0].presentCount;
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(44);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.text(`${attenDancePresentCntt}`, pageWidth / 2 - 65, 424);

    // const attenDancePresentCntttext = attendanceVennData[0].presentCount
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(18);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(`Present`, pageWidth / 2 - 85, 460);

    doc.setFillColor(164, 139, 231); // Red fill
    doc.circle(pageWidth / 2 + 65, 415, 57, 'F');
    const attenDanceLeaveCntt = attendanceVennData[0].leaveCount;
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(36);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.text(`${attenDanceLeaveCntt}`, pageWidth / 2 + 57, 408);

    // const attenDancePresentCntttext = attendanceVennData[0].presentCount
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(16);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(`Leave`, pageWidth / 2 + 48, 432);
    doc.setFillColor(249, 134, 62); // Red fill
    doc.circle(pageWidth / 2 + 46, 475, 37, 'F');
    const attenDanceAbsentCntt = attendanceVennData[0].absentCount;
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(28);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.text(`${attenDanceAbsentCntt}`, pageWidth / 2 + 38, 475);

    // const attenDancePresentCntttext = attendanceVennData[0].presentCount
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(13);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(`Absent`, pageWidth / 2 + 27, 490);

    doc.setFillColor(151, 190, 42); // Red fill

    doc.rect(pageWidth / 2 + 145, 365, 15, 15, 'F');

    doc.setTextColor(31, 58, 120);
    doc.setFontSize(12);
    doc.setFont('Helvetica', 'normal');
    doc.text('Present', pageWidth / 2 + 172, 376);

    doc.setFillColor(164, 139, 231); // Red fill
    doc.rect(pageWidth / 2 + 145, 390, 15, 15, 'F');

    doc.setTextColor(31, 58, 120);
    doc.setFontSize(12);
    doc.setFont('Helvetica', 'normal');
    doc.text('Leaves', pageWidth / 2 + 172, 401);

    doc.setFillColor(249, 134, 62); // Red fill
    doc.rect(pageWidth / 2 + 145, 415, 15, 15, 'F');

    doc.setTextColor(31, 58, 120);
    doc.setFontSize(12);
    doc.setFont('Helvetica', 'normal');
    doc.text('Absent', pageWidth / 2 + 172, 426);

    const dataRows = attendanceTable?.map(user => [
      moment(user.date).format('YYYY-MM-DD') || '-',
      user.leave_name == 'Unpaid'
        ? user.day_off == false
          ? 'Week Off'
          : user.start_time && user.end_time
            ? 'Present'
            : 'Absent'
        : user.leave_name || '-',
      user.start_time ? moment(user.start_time).format('hh:mm A') : '-',
      user.end_time ? moment(user.end_time).format('hh:mm A') : '-',
      user.open_attendance_request !== null &&
      user.open_attendance_request?.request_status == '0'
        ? 'Pending'
        : user.open_attendance_request?.request_status == '1'
          ? 'Approved'
          : user.open_attendance_request?.request_status == '2'
            ? 'Rejected'
            : '--',
      '-', // Placeholder for any additional data
      user.start_time && user.end_time
        ? moment
            .duration(moment(user.end_time).diff(moment(user.start_time)))
            .humanize()
        : '0 hrs',
      new Date(user.createdAt).toLocaleDateString(),
    ]);

    // Add table content to PDF
    let StartY = 535;
    let hasPlacedContent = false;

    doc.autoTable({
      head: [emplAttendanceTableHeaders],
      body: dataRows,
      startY: StartY,
      theme: 'grid',
      styles: {
        cellPadding: 8,

        halign: 'left',
        lineColor: [255, 255, 255], // Remove border color,
        minCellHeight: 24,
      },
      headStyles: {
        fillColor: [241, 241, 255], // Light blue color for the header background
        textColor: [31, 58, 120], // Black text color for the header
        fontSize: 11,
        font: 'Quicksand-Bold',
        fontStyle: 'normal',
      },
      bodyStyles: {
        fillColor: [241, 241, 255], // White background for odd rows
        fontSize: 10,
        font: 'Quicksand-Medium',
        fontStyle: 'normal',
      },
      alternateRowStyles: {
        fillColor: [255, 255, 255], // Light blue for even rows

        fontSize: 10,
        font: 'Quicksand-Medium',
        fontStyle: 'normal',
      },

      margin: { top: 10, right: 0, bottom: 10, left: 0 },

      columnStyles: {
        // 0: { cellPadding: { left: 10 } }, // Add extra padding to the left side of the first column
        cellPadding: { right: 80, left: 80 }, // Add extra padding to the left side of the last column
        // Add similar settings for other columns if needed
      },
    });

    doc.setFontSize(9);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.setTextColor(31, 58, 120);
    doc.text(
      '© 2014 - 2024 EmpMonitor. All rights reserved.',
      pageWidth / 2,
      pageHeight - 70,
      {
        align: 'center',
      }
    );
    const imgy02 = pageHeight - imgHeight;
    doc.addImage(imgData02, 'PNG', 0, imgy02, imgWidth, imgHeight);

    // Save the PDF
    doc.save('Consolidated_Report.pdf');
  } catch (error) {
    console.error('Error while creating PDF:', error);
  }
};
