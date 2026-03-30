import ChartProvider from 'components/ChartContext/Provider';
import jsPDF from 'jspdf';
import { useContext } from 'react';
import * as XLSX from 'xlsx';
import moment from 'moment';

export const exportPDF = (
  taskStageImg,
  taskStatusImg,
  attendanceTable,
  employeeReportData,
  employeeStatsData,
  profileImg,
  employeeTotalTaskListNum,
  taskListData,
  employeeClientChart,
  employeeClientChartData,
  employeeClientTableData,
  attendanceVennData,
  modeBike,
  employeeTravelledChart,
  employeeTravelledTableData,
  blueBike,
  picRatio,
  taskStageChartData,
  taskStatusChartData
) => {
  const doc = new jsPDF('portrait', 'pt', [595, 842]);
  let vw = window.innerWidth;

  const pageWidth = doc.internal.pageSize.getWidth();

  const pageHeight = doc.internal.pageSize.getHeight();
  let emplAttendanceTableHeaders = ['Client Name', 'Phone Number', 'Address'];
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
  const charttasksStatus = taskStatusImg;
  // const charttasksStage= taskStageImg;
  const charttaskStages = taskStageImg;

  try {
    // Add image to the top of the page

    doc.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight);
    doc.addImage(imgData03, 'PNG', 20, 44, imgWidth03, imgHeight03);
    // Add Title
    doc.setFontSize(19);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.setTextColor(31, 58, 120);
    doc.text('Employee Individual  Report', pageWidth / 2, imgHeight + 30, {
      align: 'center',
    });

    // Add Date

    const dateGet = new Date();
    const dateYear = dateGet.getFullYear();
    const dateMonth = dateGet.getMonth() + 1;
    const dateDate = dateGet.getDate();
    const dateText = `Date : ${dateDate}-${dateMonth}-${dateYear}`;

    doc.setFontSize(11);
    doc.setFont('Quicksand-Medium', 'normal');
    const textWidth = doc.getTextWidth(dateText);
    doc.text(dateText, pageWidth - textWidth - 20, imgHeight + 30);
    doc.setTextColor(31, 58, 120);

    let picSrcMain;
    const getpicwh = imgsrc => {
      if (imgsrc) {
        // Create a new Image object
        const img = new Image();

        // Set the image source to trigger loading
        img.src = imgsrc;

        // Wait for the image to load
        img.onload = () => {
          // Access the naturalWidth and naturalHeight of the image once it's loaded
          const wd = img.naturalWidth;
          const ht = img.naturalHeight;
        };
      }
    };

    if (profileImg) {
      doc.addImage(profileImg, 'JPEG', 20, 155, 48, 36 * 1.6, null, null, -0);
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

    doc.setFontSize(14);
    doc.setFont('Quicksand-Medium', 'normal');
    const filterText = 'Applied Filters';

    let currentY = 230; // Initial y position for the first filter

    const padding = 10; // Padding for the text
    const fontSize = 14; // Font size
    const rectHeight = 65; // Height of each rectangle
    let currentX = 10; // Initial x position

    const taskStageCircleData = employeeTotalTaskListNum?.overAllTags?.map(
      tag => ({
        name: tag?.tagName,
        color: tag?.color,
      })
    );

    const chartsmallcard = [
      { title: 'Tasks', col: '#78A3FF' },
      { title: 'Clients', col: '#FFAC64' },
      { title: 'Distance Travelled', col: '#FFA5C9' },
      { title: 'Net Task Value', col: '#1AB6AE' },
      { title: 'Net Task Volume', col: '#6E67CA' },
      { title: 'Mode of Transport', col: '#63D29B' },
    ];
    let taskTimeWidth;
    let taskVolumeNum;
    let taskValueNum;
    let clinetNameeewd;
    let cleintNamewidth;
    let taskValueName;
    let taskVolumeName;
    let xcod;
    let taskdateWidth;
    for (let i = 0; i < chartsmallcard.length; i++) {
      const filterText01 = chartsmallcard[i].title;

      if (i === 3) {
        doc.setFontSize(8);
        doc.setFont('Quicksand-Medium', 'normal');
      } else {
        doc.setFontSize(8);
        doc.setFont('Quicksand-Medium', 'normal');
      }
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
    const taskYcord = 390;
    const taskwidth = (pageWidth - 20) / 2;

    doc.setFillColor(164, 92, 236);
    const borderRadius = 5; // Define your border radius
    doc.roundedRect(
      currentX,
      currentY - padding - 5,
      taskwidth,
      25,
      borderRadius,
      borderRadius,
      'F'
    );

    doc.setFillColor(164, 92, 236);
    const borderRadius01 = 3; // Define your border radius
    doc.roundedRect(
      10,
      340,
      pageWidth / 2 - 15,
      30,
      borderRadius01,
      borderRadius01,
      'F'
    );

    const taskStagetext = 'Tasks Stage';

    doc.setFontSize(10);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.setTextColor(255, 255, 255);
    doc.text(taskStagetext, 20, 360);
    doc.addImage(charttaskStages, 'PNG', taskXcord, taskYcord, taskwidth, 160);

    doc.setFontSize(27);
    doc.setFont('Quicksand-SemiBold', 'normal');
    doc.setTextColor(31, 58, 120);
    doc.text(`${taskStageChartData}`, 106, 478, {
      align: 'center',
    });

    doc.setFontSize(10);
    // doc.setFont(("Quicksand-Medium","normal"));
    doc.setFont('Quicksand-Bold', 'normal');
    doc.setTextColor(31, 58, 120);

    doc.text('TOTAL STAGES', 65, 500);

    const taskXcord2 = taskXcord + taskwidth;

    doc.setFillColor(164, 92, 236);
    // const borderRadius01 = 3; // Define your border radius
    doc.roundedRect(
      taskXcord2,
      340,
      pageWidth / 2 - 10,
      30,
      borderRadius01,
      borderRadius01,
      'F'
    );
    const taskStatustext = 'Tasks Status';

    doc.setFontSize(10);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.setTextColor(255, 255, 255);
    doc.text(taskStatustext, taskXcord2 + 10, 360);
    doc.addImage(
      charttasksStatus,
      'PNG',
      taskXcord2 - 24,
      taskYcord,
      taskwidth + 50,
      172
    );

    doc.setFontSize(27);
    doc.setFont('Quicksand-SemiBold', 'normal');
    doc.setTextColor(31, 58, 120);
    doc.text(`${taskStatusChartData.taskStats.completedTask}`, 458, 462, {
      align: 'center',
    });

    doc.setFontSize(10);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.setTextColor(31, 58, 120);
    doc.text(`OUT OF ${taskStatusChartData.overAllTasks} COMPLETED`, 458, 484, {
      align: 'center',
    });

    doc.setFillColor(164, 92, 236);
    // const borderRadius01 = 3; // Define your border radius
    doc.roundedRect(
      10,
      570,
      pageWidth - 20,
      30,
      borderRadius01,
      borderRadius01,
      'F'
    );
    const taskListtext = 'Task List';

    doc.setFontSize(10);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.setTextColor(255, 255, 255);
    doc.text(taskListtext, 20, 590);

    let tasklistycord = 610;
    if (taskListData) {
      for (let i = 0; i < taskListData.length; i++) {
        let taskCardHeight;
        let taskStageYcodHeight;
        let taskStatusTextYcod;
        let taskNameTextWidth;
        let dimtext = doc.getTextDimensions(`${taskListData[i].taskName}`);
        doc.setFontSize(14);
        doc.setFont('Quicksand-Bold', 'normal');
        doc.setTextColor(31, 58, 120);
        const taskName = `${taskListData[i].taskName}`;
        taskNameTextWidth = doc.getTextWidth(taskName);

        if (taskNameTextWidth > 5200) {
          taskCardHeight = 360;
          taskStageYcodHeight = 282;
          taskStatusTextYcod = 242;
        } else if (taskNameTextWidth > 4680 && taskNameTextWidth < 5200) {
          taskCardHeight = 340;
          taskStageYcodHeight = 262;
          taskStatusTextYcod = 222;
        } else if (taskNameTextWidth > 4160 && taskNameTextWidth < 4680) {
          taskCardHeight = 320;
          taskStageYcodHeight = 242;
          taskStatusTextYcod = 202;
        } else if (taskNameTextWidth > 3640 && taskNameTextWidth < 4160) {
          taskCardHeight = 300;
          taskStageYcodHeight = 222;
          taskStatusTextYcod = 182;
        } else if (taskNameTextWidth > 3120 && taskNameTextWidth < 3640) {
          taskCardHeight = 280;
          taskStageYcodHeight = 202;
          taskStatusTextYcod = 162;
        } else if (taskNameTextWidth > 2600 && taskNameTextWidth < 3120) {
          taskCardHeight = 260;
          taskStageYcodHeight = 182;
          taskStatusTextYcod = 142;
        } else if (taskNameTextWidth > 2080 && taskNameTextWidth < 2600) {
          taskCardHeight = 240;
          taskStageYcodHeight = 162;
          taskStatusTextYcod = 122;
        } else if (taskNameTextWidth > 1560 && taskNameTextWidth < 2080) {
          taskCardHeight = 220;
          taskStageYcodHeight = 142;
          taskStatusTextYcod = 112;
        } else if (taskNameTextWidth > 1040 && taskNameTextWidth < 1560) {
          taskCardHeight = 200;
          taskStageYcodHeight = 122;
          taskStatusTextYcod = 92;
        } else if (taskNameTextWidth > 520 && taskNameTextWidth < 1040) {
          taskCardHeight = 180;
          taskStageYcodHeight = 102;
          taskStatusTextYcod = 72;
        } else {
          taskCardHeight = 160;
          taskStageYcodHeight = 82;
          taskStatusTextYcod = 42;
        }

        if (taskCardHeight > 300) {
          if (tasklistycord + taskCardHeight > pageHeight - 40) {
            // If the rectangle will go beyond the page, add a new page
            doc.addPage();
            tasklistycord = 20; // Reset y-coordinate for the new page
          }
        } else if (taskCardHeight === 280) {
          if (tasklistycord + 210 > pageHeight - 40) {
            // If the rectangle will go beyond the page, add a new page
            doc.addPage();
            tasklistycord = 20; // Reset y-coordinate for the new page
          }
        } else if (taskCardHeight === 260) {
          if (tasklistycord + 190 > pageHeight - 40) {
            // If the rectangle will go beyond the page, add a new page
            doc.addPage();
            tasklistycord = 20; // Reset y-coordinate for the new page
          }
        } else if (taskCardHeight === 240) {
          if (tasklistycord + 170 > pageHeight - 40) {
            // If the rectangle will go beyond the page, add a new page
            doc.addPage();
            tasklistycord = 20; // Reset y-coordinate for the new page
          }
        } else if (taskCardHeight === 220) {
          if (tasklistycord + 150 > pageHeight - 40) {
            // If the rectangle will go beyond the page, add a new page
            doc.addPage();
            tasklistycord = 20; // Reset y-coordinate for the new page
          }
        } else if (taskCardHeight === 200) {
          if (tasklistycord + 130 > pageHeight - 40) {
            // If the rectangle will go beyond the page, add a new page
            doc.addPage();
            tasklistycord = 20; // Reset y-coordinate for the new page
          }
        } else if (taskCardHeight === 180) {
          if (tasklistycord + 110 > pageHeight - 40) {
            // If the rectangle will go beyond the page, add a new page
            doc.addPage();
            tasklistycord = 20; // Reset y-coordinate for the new page
          }
        } else {
          if (tasklistycord + 95 > pageHeight - 40) {
            // If the rectangle will go beyond the page, add a new page
            doc.addPage();
            tasklistycord = 20; // Reset y-coordinate for the new page
          }
        }

        doc.setFillColor(247, 247, 250);
        const borderRadius = 5; // Define your border radius
        doc.roundedRect(
          20,
          tasklistycord,
          pageWidth - 40,
          taskCardHeight,
          borderRadius,
          borderRadius,
          'F'
        );

        if (
          employeeTotalTaskListNum &&
          employeeTotalTaskListNum.userTaskDetails &&
          employeeTotalTaskListNum.userTaskDetails[i]
        ) {
          doc.setFontSize(9);
          doc.setFont('Quicksand-Medium', 'normal');
          doc.setTextColor(31, 58, 120);
          const taskDate = `${taskListData[i].date}`;
          xcod = 35;
          taskdateWidth = doc.getTextWidth(taskDate);

          doc.text(taskDate, 35, tasklistycord + 18);

          doc.setFontSize(9);
          doc.setFont('Quicksand-SemiBold', 'normal');
          doc.setTextColor(31, 58, 120);
          xcod = taskdateWidth + 5 + xcod;

          const taskTimeInt = `  Start Time : 04:59 PM  End Time : 06:34 PM`;

          doc.text(taskTimeInt, 82, tasklistycord + 18);
          taskTimeWidth = doc.getTextWidth(
            '  Start Time : 04:59 PM  End Time : 06:34 PM'
          );
          xcod = taskTimeWidth + 88;
          doc.setFontSize(14);
          doc.setFont('Quicksand-Bold', 'normal');
          doc.setTextColor(31, 58, 120);
          const taskName = `${employeeTotalTaskListNum.userTaskDetails[i].taskName}`;

          doc.text(taskName, 35, tasklistycord + 43, { maxWidth: 520 });

          let TaskStatusXcod;
          if (taskCardHeight === 160) {
            TaskStatusXcod = 62 + doc.getTextWidth(taskName);
          } else {
            TaskStatusXcod = 60;
          }
          if (
            employeeTotalTaskListNum.userTaskDetails[i].taskApproveStatus == 2
          ) {
            doc.setFillColor(255, 89, 96); // Red fill
            doc.roundedRect(
              20,
              tasklistycord,
              7,
              taskCardHeight,
              5,
              borderRadius,

              'F'
            );
            doc.setFillColor(255, 89, 96); // Red fill
            doc.circle(
              TaskStatusXcod - 10,
              tasklistycord + taskStatusTextYcod - 4,
              3,
              'F'
            );
            doc.setFontSize(9);
            doc.setFont('Quicksand-SemiBold', 'normal');
            doc.setTextColor(31, 58, 120);

            const taskStatus = `Pause`;
            doc.text(
              taskStatus,
              TaskStatusXcod,
              tasklistycord + taskStatusTextYcod
            );
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i].taskApproveStatus == 0
          ) {
            doc.setFillColor(255, 184, 0); // Red fill
            doc.roundedRect(
              20,
              tasklistycord,
              7,
              taskCardHeight,
              5,
              borderRadius,

              'F'
            );
            doc.setFillColor(255, 184, 0); // Red fill
            doc.circle(
              TaskStatusXcod - 10,
              tasklistycord + taskStatusTextYcod - 4,
              3,
              'F'
            );
            doc.setFontSize(9);
            doc.setFont('Quicksand-SemiBold', 'normal');
            doc.setTextColor(31, 58, 120);
            const taskStatus = `Pending`;
            doc.text(
              taskStatus,
              TaskStatusXcod,
              tasklistycord + taskStatusTextYcod
            );
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i].taskApproveStatus == 1
          ) {
            doc.setFillColor(255, 89, 96); // Red fill
            doc.roundedRect(
              20,
              tasklistycord,
              7,
              taskCardHeight,
              5,
              borderRadius,

              'F'
            );
            doc.setFillColor(255, 89, 96); // Red fill
            doc.circle(
              TaskStatusXcod - 10,
              tasklistycord + taskStatusTextYcod - 4,
              3,
              'F'
            );
            doc.setFontSize(9);
            doc.setFont('Quicksand-SemiBold', 'normal');
            doc.setTextColor(31, 58, 120);
            const taskStatus = `Start`;
            doc.text(
              taskStatus,
              TaskStatusXcod,
              tasklistycord + taskStatusTextYcod
            );
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i].taskApproveStatus == 3
          ) {
            doc.setFillColor(255, 89, 96); // Red fill
            doc.roundedRect(
              20,
              tasklistycord,
              7,
              taskCardHeight,
              5,
              borderRadius,

              'F'
            );
            doc.setFillColor(255, 89, 96); // Red fill
            doc.circle(
              TaskStatusXcod - 10,
              tasklistycord + taskStatusTextYcod - 4,
              3,
              'F'
            );
            doc.setFontSize(9);
            doc.setFont('Quicksand-SemiBold', 'normal');
            doc.setTextColor(31, 58, 120);
            const taskStatus = `Start`;
            doc.text(
              taskStatus,
              TaskStatusXcod,
              tasklistycord + taskStatusTextYcod
            );
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i].taskApproveStatus == 4
          ) {
            doc.setFillColor(77, 185, 72); // Red fill
            doc.roundedRect(
              20,
              tasklistycord,
              7,
              taskCardHeight,
              5,
              borderRadius,

              'F'
            );
            doc.setFillColor(77, 185, 72); // Red fill
            doc.circle(
              TaskStatusXcod - 10,
              tasklistycord + taskStatusTextYcod - 4,
              3,
              'F'
            );
            doc.setFontSize(9);
            doc.setFont('Quicksand-SemiBold', 'normal');
            doc.setTextColor(31, 58, 120);
            const taskStatus = `Finish`;
            doc.text(
              taskStatus,
              TaskStatusXcod,
              tasklistycord + taskStatusTextYcod
            );
          } else {
            doc.setFillColor(255, 89, 96); // Red fill
            doc.roundedRect(
              20,
              tasklistycord,
              10,
              taskCardHeight,
              borderRadius,
              borderRadius,
              'F'
            );
            doc.setFillColor(255, 89, 96); // Red fill
            doc.circle(
              TaskStatusXcod - 10,
              tasklistycord + taskStatusTextYcod - 4,
              3,
              'F'
            );
            doc.setFontSize(9);
            doc.setFont('Quicksand-SemiBold', 'normal');
            doc.setTextColor(31, 58, 120);
            const taskStatus = `Delete`;
            doc.text(
              taskStatus,
              TaskStatusXcod,
              tasklistycord + taskStatusTextYcod
            );
          }
        }

        doc.setFontSize(9);
        doc.setFont('Quicksand-SemiBold', 'normal');
        doc.setTextColor(31, 58, 120);

        doc.text('Task Stage', 42, tasklistycord + taskStageYcodHeight);
        doc.setFillColor(255, 172, 100), doc.setFontSize(8);
        doc.setTextColor(255, 172, 100);
        doc.setFont('Quicksand-Medium', 'normal');

        doc.text('Client Name : ', xcod + 5, tasklistycord + 18);
        clinetNameeewd = doc.getTextWidth('Client Name : ');
        xcod = xcod + 6 + clinetNameeewd;
        doc.setFontSize(8);
        doc.setFont('Quicksand-Medium', 'normal');
        doc.setTextColor(255, 172, 100);
        if (employeeTotalTaskListNum.userTaskDetails[i]) {
          const taskStatsData00 = `${employeeTotalTaskListNum.userTaskDetails[i]?.clientData[0]?.clientName}`;

          doc.text(taskStatsData00, xcod, tasklistycord + 18);
          cleintNamewidth = doc.getTextWidth(taskStatsData00);
          xcod = xcod + cleintNamewidth + 6;
        }
        doc.setFontSize(8);
        doc.setTextColor(26, 182, 174);
        doc.setFont('Quicksand-Medium', 'normal');
        doc.text('Task Value : ', xcod, tasklistycord + 18);
        taskValueName = doc.getTextWidth('Task Value : ');
        xcod = xcod + 2 + taskValueName;

        if (
          employeeTotalTaskListNum &&
          employeeTotalTaskListNum.userTaskDetails &&
          employeeTotalTaskListNum.userTaskDetails[i]
        ) {
          let taskStatsData0000 = `${employeeTotalTaskListNum.userTaskDetails[i].value.convertedAmountInUSD == null ? '0' : employeeTotalTaskListNum.userTaskDetails[i].value.convertedAmountInUSD}`;
          doc.setFontSize(8);
          doc.setFont('Quicksand-Medium', 'normal');
          doc.setTextColor(26, 182, 174);
          const textxcod = 455;
          doc.text(taskStatsData0000, xcod, tasklistycord + 18);
          doc.setFontSize(8);
          doc.setFont('Quicksand-Medium', 'normal');
          doc.setTextColor(26, 182, 174);
          const currencyCode = `${'$'}`;

          taskValueNum = doc.getTextWidth(taskStatsData0000);
          doc.text(currencyCode, xcod + taskValueNum, tasklistycord + 18);
          xcod = xcod + taskValueNum + 9;
        }

        doc.setFillColor(110, 103, 202),
          //
          doc.setFontSize(8);
        doc.setTextColor(110, 103, 202);
        doc.setFont('Quicksand-Medium', 'normal');
        doc.text('Task Volume : ', xcod, tasklistycord + 18);
        taskVolumeName = doc.getTextWidth('Task Volume : ');
        xcod = xcod + taskVolumeName;
        doc.setFontSize(8);
        doc.setFont('Quicksand-Medium', 'normal');

        const taskStatsData = `${employeeTotalTaskListNum.userTaskDetails[i]?.taskVolume || 0}`;
        doc.setTextColor(110, 103, 202);
        doc.text(taskStatsData, xcod, tasklistycord + 18);

        doc.setFillColor(255, 255, 255);

        doc.roundedRect(
          40,
          tasklistycord + taskStageYcodHeight + 10,
          pageWidth - 70,
          57,
          borderRadius,
          borderRadius,
          'F'
        );

        function hexToRgb(hex) {
          const bigint = parseInt(hex.slice(1), 16);
          return {
            r: (bigint >> 16) & 255,
            g: (bigint >> 8) & 255,
            b: bigint & 255,
          };
        }
        let taskStageTimelinecirXcode = 65;
        for (let j = 0; j < taskStageCircleData.length; j++) {
          const { color, name } = taskStageCircleData[j];

          // Extract color and parse it for RGB values
          const rgb = hexToRgb(color);

          if (rgb) {
            doc.setFillColor(rgb.r, rgb.g, rgb.b); // Set circle color from tag's color
            doc.circle(
              taskStageTimelinecirXcode,
              tasklistycord + taskStageYcodHeight + 30,
              7,
              'F'
            );
          }

          if (j < 9) {
            doc.setFontSize(19);
            doc.setFont('Quicksand-Medium', 'normal');
            doc.setTextColor(31, 58, 120);
            doc.text(
              `......`,
              taskStageTimelinecirXcode + 12,
              tasklistycord + taskStageYcodHeight + 30
            );
          }
          doc.setFontSize(10);
          doc.setFont('Quicksand-SemiBold', 'normal');
          doc.setTextColor(31, 58, 120);

          const textDisplay = `${name}`;
          const splitText = doc.splitTextToSize(textDisplay, 55); // 40 is max width in px
          doc.text(
            splitText,
            taskStageTimelinecirXcode,
            tasklistycord + taskStageYcodHeight + 50,
            {
              align: 'center',
            }
          );

          // Additional check mark logic
          doc.setFontSize(20);
          doc.setFont('Quicksand-Medium', 'normal');
          doc.setTextColor(31, 58, 120);

          taskStageTimelinecirXcode += 53;
        }
        let checkMark =
          'iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAACXBIWXMAAA7EAAAOxAGVKw4bAAADfklEQVR4nO3bQYhVZRTA8XNNwoYYRGKIkJAQEQkZTCrCNhItXIlIhEi0kIgoiYgQt64kJKRaSkRERES0CAkX0cpNUBZuchEiNQ02TBIS0zD8Wtz3Yhx6vnnf9933zVj//fvO+Z9777vfPefeiP84Te0ESoNNEXE0Ih6JiN8j4qOmab6vm9WYwAZ87lYW8WavMHc2eNVgLmNP7Rw7A9P48zYFgAWcwIba+RYFE/hhiPxyLuD+2nkXA2+NIN/nGrbdVTv5XPBURLwTo9/RJiNial0XAJsj4nxEbE5c4p71/mdwNiIezPj9L6USGTs4mHDdr+RQbY8kMIXZTPlPanskg88y5WcxVdsjCTyXKQ+Ha3skga2Yy5T/uLZHEtoHnS8z5WdwX22XJPBipjwcrO2RBLbjj0z5D2p7JKE99b/OlP8ZW2q7JOH2z/irYQkHanskgZ24mVmAc7U9ksBGXMyU/wmTtV2SwMlM+SXsr+2RBHYb3t4axru1PZLA3fg2U/4K7q3tkgROZcovYl9tjySwtyeQw5naHklgk7Z3n8NlTNR2SUI7vclhEY/W9kgC+7S3rRxOpcQe2ErWdkxeiYgdEfFjRJxtmua3NMXB9E7ZSxGxPWOZ7yLisaZp/iqV1C5cXVHhK9hWJMCtsd7OPPIL2F0yoWmDG47XsKNgrP3yT/0TpfIJ7DG85TSDXQViTWr36jlcxMYS7v0jv9p+26zM0w7nMuVvYmcp+YdxfcQErkuct+NApjwcLyX/gPbaTmEOe0eMt0XbocnhK6Vm/HgvM5l5I2xA8GFmvBt4qIh8L6Hc7SerLAIOFYh1rJh8L6nzBZJiSBGUmed9ofTrLdpt6EJmYn3m8fiAOJ9mrj2HrUXllyV3WP5jaJ8beGLF+kcKrHukE/llSR6Vvyvr808RtHeZ3HneeEbZeEHhImiv2xxmjHOUjZeUK0KJ/5bxz/NwXLki5PD+2OWXFeE1dYtwVfsWWD3weiX5JTxdVb4P3qhQgLU11BhzEdbmUGNMRVi0YhO1phhDEU7XdhyK7u4Ol6yXrzo6KMICpmt7jUThIpys7ZOEMjvGcp3dGmQWoVxntybSH6Berp17MYz+KH3BHfj11vNW11ma11V7qzZ4xvA+wLO18+wU7dDz3yZNSxLn+F3R2cfT2tfSj0XEkxExERG/RsSZpmm+6Srm/yTwNzVPsQQxJRkEAAAAAElFTkSuQmCC'; // Or use '✔' for a different style

        if (
          employeeTotalTaskListNum.userTaskDetails[i]?.tagLogsWithColors
            .length > 0
        ) {
          if (
            employeeTotalTaskListNum.userTaskDetails[i]?.tagLogsWithColors[0]
              .tagName === 'Restart'
          ) {
            doc.addImage(`${checkMark}`, 'PNG', 62, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 115, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 168, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 221, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 274, tasklistycord + 108, 7, 7);
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i]?.tagLogsWithColors[0]
              .tagName === 'conslusion w'
          ) {
            doc.addImage(`${checkMark}`, 'PNG', 62, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 115, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 168, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 221, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 274, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 327, tasklistycord + 108, 7, 7);
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i]?.tagLogsWithColors[0]
              .tagName === 'Closure'
          ) {
            doc.addImage(`${checkMark}`, 'PNG', 62, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 115, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 168, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 221, tasklistycord + 108, 7, 7);
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i]?.tagLogsWithColors[0]
              .tagName === 'Discussion'
          ) {
            doc.addImage(`${checkMark}`, 'PNG', 62, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 115, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 168, tasklistycord + 108, 7, 7);
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i]?.tagLogsWithColors[0]
              .tagName === 'Processing'
          ) {
            doc.addImage(`${checkMark}`, 'PNG', 62, tasklistycord + 108, 7, 7);
            doc.addImage(`${checkMark}`, 'PNG', 115, tasklistycord + 108, 7, 7);
          } else if (
            employeeTotalTaskListNum.userTaskDetails[i]?.tagLogsWithColors[0]
              .tagName === 'backlog'
          ) {
            doc.addImage(`${checkMark}`, 'PNG', 62, tasklistycord + 108, 7, 7);
          }
        }
        // }
        // if (
        //   employeeTotalTaskListNum.userTaskDetails[i]?.tagLogsWithColors?.length >
        //   0
        // ) {
        //   // Iterate over tagLogsWithColors to find the 'Planning' tag
        //   for (let tag of employeeTotalTaskListNum.userTaskDetails[i]
        //     .tagLogsWithColors) {
        //     if (tag?.tagName === 'Planning') {
        //       doc.addImage(`${checkMark}`, 'PNG', 115, tasklistycord + 108, 7, 7);
        //       // break; // Exit loop once 'Planning' is found
        //     } else if (tag?.tagName === 'Discussion') {
        //       doc.addImage(`${checkMark}`, 'PNG', 168, tasklistycord + 108, 7, 7);
        //     } else if (tag?.tagName === 'support') {
        //       doc.addImage(`${checkMark}`, 'PNG', 274, tasklistycord + 108, 7, 7);
        //     }
        //   }
        // }
        let TaskStageActiveDone = 0;

        tasklistycord += taskCardHeight + 12;
      }
    } else {
      doc.setFontSize(12);
      doc.setFont('Quicksand-Bold', 'normal');
      doc.setTextColor(31, 58, 120);
      doc.text(' No Task Data is Available', pageWidth / 2, tasklistycord + 6, {
        align: 'center',
      });

      tasklistycord = 630;
    }

    if (tasklistycord > pageHeight - 150) {
      doc.addPage();
      tasklistycord = 10;
    }

    doc.setFillColor(164, 92, 236);

    doc.roundedRect(10, tasklistycord + 13, pageWidth - 20, 30, 3, 3, 'F');

    doc.setTextColor(255, 255, 255);
    doc.setFontSize(10);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.text('Clients', 20, tasklistycord + 30);

    if (employeeClientTableData.length > 0) {
      doc.addImage(employeeClientChart, 40, tasklistycord + 60, 520, 120);
      doc.setTextColor(31, 58, 120);
      doc.setFontSize(27);
      doc.setFont('Quicksand-SemiBold', 'normal');
      doc.text(
        `${employeeClientChartData.clientCounts.contacted}`,
        256,
        tasklistycord + 132
      );

      doc.setTextColor(31, 58, 120);
      doc.setFontSize(10);
      doc.setFont('Quicksand-Bold', 'normal');
      doc.text(
        `OUT OF ${employeeClientChartData.clientCounts.contacted + employeeClientChartData.clientCounts.notContacted} TOTAL`,
        224,
        tasklistycord + 148
      );
      tasklistycord = tasklistycord + 195;
      const dataRows = employeeClientTableData?.map(user => [
        user.clientName,
        user.phoneNumber,
        user.address,
      ]);

      // Add table content to PDF
      let StartY = tasklistycord;
      let hasPlacedContent = false;
      let tableHeightClients = 0;

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
        didDrawPage: data => {
          tableHeightClients = data.cursor.y - StartY; // Calculate the table height
        },
      });
      tasklistycord = tasklistycord + tableHeightClients + 40;
    } else {
      tasklistycord = tasklistycord + 60;
    }

    if (!employeeClientTableData || employeeClientTableData.length === 0) {
      doc.setFontSize(12);
      doc.setFont('Quicksand-Bold', 'normal');
      doc.setTextColor(31, 58, 120);
      doc.text(' No Data is Available', pageWidth / 2, tasklistycord, {
        align: 'center',
      });
    }

    if (tasklistycord > pageHeight - 270) {
      doc.addPage();
      tasklistycord = 20;
    }

    doc.setFillColor(164, 92, 236);

    doc.roundedRect(10, tasklistycord + 13, pageWidth - 20, 30, 3, 3, 'F');
    const taskAttendancetext = 'Attendance';

    doc.setFontSize(10);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.setTextColor(255, 255, 255);
    doc.text(taskAttendancetext, 20, tasklistycord + 33);

    doc.setFillColor(151, 190, 42); // Red fill425

    doc.circle(pageWidth / 2 - 50, tasklistycord + 150, 96, 'F');
    const attenDancePresentCntt = attendanceVennData[0].presentCount;
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(44);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(
      `${attenDancePresentCntt}`,
      pageWidth / 2 - 76,
      tasklistycord + 148
    );

    doc.setTextColor(255, 255, 255);
    doc.setFontSize(18);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(`Present`, pageWidth / 2 - 80, tasklistycord + 168);

    doc.setFillColor(164, 139, 231); // Red fill
    doc.circle(pageWidth / 2 + 65, tasklistycord + 120, 57, 'F');
    const attenDanceLeaveCntt = attendanceVennData[0].leaveCount;
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(36);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(`${attenDanceLeaveCntt}`, pageWidth / 2 + 57, tasklistycord + 107);

    doc.setTextColor(255, 255, 255);
    doc.setFontSize(16);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(`Leave`, pageWidth / 2 + 48, tasklistycord + 136);
    doc.setFillColor(249, 134, 62); // Red fill
    doc.circle(pageWidth / 2 + 46, tasklistycord + 180, 37, 'F');
    const attenDanceAbsentCntt = attendanceVennData[0].absentCount;
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(28);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(
      `${attenDanceAbsentCntt}`,
      pageWidth / 2 + 38,
      tasklistycord + 180
    );

    doc.setTextColor(255, 255, 255);
    doc.setFontSize(13);
    doc.setFont('Quicksand-Medium', 'normal');
    doc.text(`Absent`, pageWidth / 2 + 27, tasklistycord + 195);

    tasklistycord = 260 + tasklistycord;

    if (tasklistycord > pageHeight - 270) {
      doc.addPage();
      tasklistycord = 20;
    }

    const dataRowsAttendance = attendanceTable?.map(user => [
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

    let hasPlacedContentAttendance = false;
    const emplAttendanceTableHeadersMain = [
      'Date',
      'Status',
      'Check In',
      'Check Out',
      'Request Status',
      'Overridden By',
      'Total',
    ];
    let StartY2 = tasklistycord;
    let lastPageHeight = 0;
    let tableHeightClients2;
    doc.autoTable({
      head: [emplAttendanceTableHeadersMain],
      body: dataRowsAttendance,
      startY: StartY2,
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
      didDrawPage: function (data) {
        tableHeightClients2 = data.cursor.y; // Calculate the table height

        tasklistycord = tableHeightClients2;
      },
    });

    if (tasklistycord > pageHeight - 70) {
      doc.addPage();
      tasklistycord = 20;
    }

    doc.setFillColor(164, 92, 236);

    doc.roundedRect(10, tasklistycord + 23, pageWidth - 20, 30, 3, 3, 'F');

    doc.setTextColor(255, 255, 255);
    doc.setFontSize(10);
    doc.setFont('Quicksand-Bold', 'normal');
    doc.text('Distance Travelled', 20, tasklistycord + 43);

    if (employeeTravelledTableData.length > 0) {
      if (tasklistycord > pageHeight - 300) {
        doc.addPage();
        tasklistycord = 20;
      }
      doc.addImage(employeeTravelledChart, 90, tasklistycord + 55, 380, 260);

      tasklistycord += 260;
      if (tasklistycord > pageHeight - 300) {
        doc.addPage();
        tasklistycord = 20;
      }

      let emplTravelledTableHeaders = [
        'Date',
        'Distance Travelled (km)',
        'Mode',
        'Frequency (Seconds)',
      ];
      const dataRowsTravelled = employeeTravelledTableData?.map(user => [
        // moment(user.date).format('YYYY-MM-DD') || '-',
        user.date,
        user.distTravelled?.toFixed(2) || '-',
        null,
        `${user.currentFrequency}`,
      ]);

      // Add table content to PDF

      doc.autoTable({
        head: [emplTravelledTableHeaders],
        body: dataRowsTravelled,
        startY: tasklistycord + 40,
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
        didDrawCell: data => {
          const columnIndex = data.column.index;
          const rowIndex = data.row.index;

          // Check if this is the column where the image should go
          if (columnIndex === 2) {
            // Assuming column index 2 is for images
            const user = employeeTravelledTableData[rowIndex];

            // Adjust X and Y as per the cell dimensions for centering or positioning the image

            if (data.section === 'body' && columnIndex === 2) {
              doc.addImage(
                blueBike,
                'PNG',
                data.cell.x + 7,
                data.cell.y + 2,
                15,
                15
              );
            }
          }
        },
      });
    } else {
      doc.setFontSize(12);
      doc.setFont('Quicksand-Bold', 'normal');
      doc.setTextColor(31, 58, 120);
      doc.text(' No Data is Available', pageWidth / 2, tasklistycord + 70, {
        align: 'center',
      });
      tasklistycord = tasklistycord + 90;
    }

    if (tasklistycord > pageHeight - 70) {
      doc.addPage();
      tasklistycord = 20;
    }

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
