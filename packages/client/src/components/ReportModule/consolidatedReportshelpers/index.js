import jsPDF from 'jspdf';
import * as XLSX from 'xlsx';

export const exportPDF = (reportDetails, selectedFilters, headers) => {
  const doc = new jsPDF('portrait', 'pt', 'a4');

  const pageWidth = doc.internal.pageSize.getWidth();
  const pageHeight = doc.internal.pageSize.getHeight();

  // Add images as background
  const imgData =
    'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/header-2.png';
  const imgData02 =
    'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/header-1.png';
  const imgData03 =
    'https://storage.googleapis.com/emp-field-tracking-live/fieldTracking/EmpMonitor-logo-tag1-white.png';

  const imgWidth = pageWidth;
  const imgHeight = 100;
  const imgWidth03 = 148;
  const imgHeight03 = 25;

  try {
    // Add image to the top of the page
    doc.addImage(imgData, 'PNG', 0, 0, imgWidth, imgHeight);
    doc.addImage(imgData03, 'PNG', 20, 44, imgWidth03, imgHeight03);

    // Add Title
    doc.setFontSize(19);
    doc.setFont('Quicksand', 'bold');
    doc.setTextColor(31, 58, 120);
    doc.text('CONSOLIDATED REPORT', pageWidth / 2, imgHeight + 20, {
      align: 'center',
    });

    // Function to separate dates and other filter details
    function separateDatesAndOthers(dataObject) {
      let startDates = [];
      let endDates = [];
      let others = [];

      if (dataObject?.dateRange) {
        const { startDate, endDate } = dataObject.dateRange;
        if (startDate && endDate) {
          startDates.push(startDate);
          endDates.push(endDate);
        }
      }

      Object.keys(dataObject).forEach(key => {
        if (key !== 'dateRange') {
          const value = dataObject[key];
          if (value && typeof value === 'string' && value.trim() !== '') {
            others.push(`${key}: ${value}`);
          }
        }
      });

      return { startDates, endDates, others };
    }

    const separatedData = separateDatesAndOthers(selectedFilters);

    // Display 'Applied Filters'
    const filterText = 'Applied Filters';
    doc.setFontSize(13);
    doc.setFont('Quicksand', 'normal');
    doc.setTextColor(31, 58, 120);
    doc.text(filterText, 20, imgHeight + 50);

    let currentY = imgHeight + 75;
    const padding = 10;
    const fontSize = 10;
    const rectHeight = 25;
    let currentX = 20;

    // Display filters as rectangles with text inside
    separatedData?.others?.forEach(filter => {
      const textWidth = doc.getTextWidth(filter);
      const rectWidth = textWidth + 2 * padding;

      // if (currentX + rectWidth > pageWidth - 20) {
      //   currentX = 20; // Reset to left margin
      //   currentY += rectHeight + 10; // Move down to the next row
      // }

      // if (currentY + rectHeight > pageHeight - 20) {
      //   doc.addPage();
      //   currentY = 20;
      //   currentX = 20;
      // }

      doc.setFillColor(241, 241, 255);
      doc.rect(currentX, currentY - padding - 5, rectWidth, rectHeight, 'F');

      doc.setFontSize(fontSize);
      doc.setTextColor(106, 106, 236);
      doc.text(
        filter,
        currentX + rectWidth / 2,
        currentY + rectHeight / 2 - padding - 5,
        {
          align: 'center',
        }
      );

      currentX += rectWidth + 10;
    });

    // Prepare table data
    const dataRows = reportDetails?.map(user => [
      user?.fullName || '-',
      user?.email || '-',
      user?.TasksFinished || 0,
      user?.TasksPending || 0,
      user?.TasksPaused || 0,
      user?.TasksResumed || 0,
      user?.Clients || '0',
      user?.role || '-',
      user?.department || '-',
      user?.location || '-',
      new Date(user.createdAt).toLocaleDateString(),
    ]);

    // Add table content to PDF
    doc.autoTable({
      head: headers,
      body: dataRows,
      startY: imgHeight + 120,
      theme: 'grid',
      styles: {
        cellPadding: 8,
        halign: 'left',
        lineColor: [255, 255, 255],
        minCellHeight: 24,
      },
      headStyles: {
        fillColor: [241, 241, 255],
        textColor: [31, 58, 120],
        fontSize: 9,
      },
      bodyStyles: {
        fillColor: [241, 241, 255],
        fontSize: 8,
      },
      alternateRowStyles: {
        fillColor: [255, 255, 255],
        fontSize: 8,
      },
      margin: { top: 10, right: 0, bottom: 10, left: 0 },
    });

    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(31, 58, 120);
    doc.text(
      '© 2014 - 2024 EmpMonitor. All rights reserved.',
      pageWidth / 2,
      pageHeight - 70,
      { align: 'center' }
    );

    const imgy02 = pageHeight - imgHeight;
    doc.addImage(imgData02, 'PNG', 0, imgy02, imgWidth, imgHeight);

    // Save the PDF
    doc.save('Consolidated_Report.pdf');
  } catch (error) {
    console.error('Error while creating PDF:', error);
  }
};

// export const exportCSV = (reportDetails, csvHeaders) => {
//   const csvRows = reportDetails?.map(user =>
//     [
//       user.fullName || '-',
//       user.email || '-',
//       // user.taskVolume || 0,
//       user?.finishTaskCount?.count || 0,
//       user?.pendingTaskCount?.count || 0,
//       user?.pauseTaskCount?.count || 0,
//       user?.resumeTaskCount?.count || 0,
//       user?.uniqueClientCount || '0',
//       user.role || '-',
//       user.department || '-',
//       user.location || '-',
//       new Date(user.createdAt).toLocaleDateString(),
//     ].join(',')
//   );

//   const csvContent = [csvHeaders, ...csvRows].join('\n');

//   const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
//   const link = document.createElement('a');
//   if (link.download !== undefined) {
//     // feature detection
//     const url = URL.createObjectURL(blob);
//     link.setAttribute('href', url);
//     link.setAttribute('download', 'Consolidated_Report.csv');
//     link.style.visibility = 'hidden';
//     document.body.appendChild(link);
//     link.click();
//     document.body.removeChild(link);
//   }
// };

// export const exportXLS = (reportDetails, headers) => {
//   console.log(reportDetails,'reportDetails')
//   // Define headers and data

//   const data = reportDetails && reportDetails?.map(user => [
//     user.fullName || '-',
//     user.email || '-',
//     // user.taskVolume || 0,
//     user?.finishTaskCount?.count || 0,
//     user?.pendingTaskCount?.count || 0,
//     user?.pauseTaskCount?.count || 0,
//     user?.resumeTaskCount?.count || 0,
//     user?.uniqueClientCount || '0',
//     user.role || '-',
//     user.department || '-',
//     user.location || '-',
//     new Date(user.createdAt).toLocaleDateString(),
//   ]);

//   // Create worksheet
//   const ws = XLSX.utils.aoa_to_sheet([headers, ...data]);

//   // Create workbook and add the worksheet
//   const wb = XLSX.utils.book_new();
//   XLSX.utils.book_append_sheet(wb, ws, 'Consolidated Report');

//   // Write workbook to file
//   XLSX.writeFile(wb, 'Consolidated_Report.xlsx');
// };
const convertHeaderToField = header => {
  return header
    .toLowerCase() // Convert to lowercase
    .replace(/[^a-zA-Z0-9]+(.)/g, (match, char) => char.toUpperCase()); // Convert spaces or non-alphanumeric characters to camelCase
};
const flattenArray = arr => {
  return arr.flat();
};

// Function to generate a dynamic header to field mapping
const generateHeaderToFieldMap = headers => {
  return headers.reduce((map, header) => {
    const field = convertHeaderToField(header);
    map[header] = field;
    return map;
  }, {});
};

export const exportXLS = (reportDetails, headers, reportName) => {
  // Generate the dynamic header-to-field mapping based on provided headers
  const headerToFieldMap = generateHeaderToFieldMap(headers);

  // Define data dynamically based on inferred fields
  const data = reportDetails?.map(user =>
    headers.map(header => {
      const field = headerToFieldMap[header];

      // Handle dynamic values, including nested fields or counts
      const value = field
        ? field
            .split('.')
            .reduce(
              (obj, key) =>
                obj !== undefined && obj !== null ? obj[key] : '-',
              user
            )
        : user[field] || '-'; // Fallback to '-' if field is not defined

      // Convert date fields properly
      if (field === 'createdAt' && user.createdAt) {
        return new Date(user.createdAt).toLocaleDateString();
      }

      // For counts, ensure you are correctly accessing the user properties
      // if (field === 'overallTaskCount') {
      //   return user.overallTaskCount || '-';
      // }
      if (field === 'tasksFinished') {
        return user.TasksFinished || '-';
      }
      if (field === 'tasksPending') {
        return user.TasksPending || '-';
      }
      if (field === 'tasksPaused') {
        return user.TasksPaused || '-';
      }
      if (field === 'tasksResumed') {
        return user.TasksResumed || '-';
      }

      if (field === 'clients') {
        return user.Clients || '-';
      }

      return value !== undefined && value !== null ? value : '-';
    })
  );

  // Create worksheet
  const ws = XLSX.utils.aoa_to_sheet([headers, ...data]);

  // Create workbook and add the worksheet
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, reportName);

  // Write workbook to file
  XLSX.writeFile(wb, reportName + '.xlsx');
};
const convertHeaderToFieldCsv = header => {
  if (typeof header !== 'string') {
    console.warn('Expected header to be a string, but received:', header);
    return '';
  }

  return header
    .toLowerCase()
    .replace(/[^a-zA-Z0-9]+(.)/g, (match, char) => char.toUpperCase());
};

const generateHeaderToFieldMapCsv = headers => {
  return headers.reduce((map, header) => {
    const field = convertHeaderToFieldCsv(header);
    map[header] = field;
    return map;
  }, {});
};

export const exportCSV = (reportDetails, headers, fileName) => {
  // Flatten headers if nested
  const flatHeaders = flattenArray(headers);

  // Generate the dynamic header-to-field mapping based on provided headers
  const headerToFieldMap = generateHeaderToFieldMapCsv(flatHeaders);

  // Map report details to rows dynamically
  const csvRows = reportDetails?.map(user => {
    return flatHeaders
      .map(header => {
        const field = headerToFieldMap[header];

        // Handle dynamic values, including nested fields or counts
        const value = field
          ? field
              .split('.')
              .reduce(
                (obj, key) =>
                  obj !== undefined && obj !== null ? obj[key] : '-',
                user
              )
          : user[field] || '-'; // Fallback to '-' if field is not defined

        // Convert date fields properly
        if (field === 'createdAt' && user.createdAt) {
          return new Date(user.createdAt).toLocaleDateString();
        }

        // Handle specific cases for counts or custom fields
        if (field === 'tasksFinished') {
          return user.TasksFinished || '-';
        }
        if (field === 'tasksPending') {
          return user.TasksPending || '-';
        }
        if (field === 'tasksPaused') {
          return user.TasksPaused || '-';
        }
        if (field === 'tasksResumed') {
          return user.TasksResumed || '-';
        }
        if (field === 'clients') {
          return user.Clients || '-';
        }

        return value !== undefined && value !== null ? value : '-';
      })
      .join(','); // Join each field value into a single CSV row
  });

  // Create the CSV content with headers as the first row
  const csvContent = [flatHeaders.join(','), ...csvRows].join('\n');

  // Generate the CSV file and initiate download
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement('a');
  if (link.download !== undefined) {
    // Feature detection
    const url = URL.createObjectURL(blob);
    link.setAttribute('href', url);
    link.setAttribute('download', fileName + '.csv');
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
};
