import { jsPDF } from 'jspdf';
import * as autoTable from 'jspdf-autotable';

export const handleEmpolyeeReport = (
  headers,
  formattedData,
  employeePdfDownloadTitle
) => {
  const doc = new jsPDF();

  // Draw the border
  const pageWidth = doc.internal.pageSize.getWidth();
  const pageHeight = doc.internal.pageSize.getHeight();
  const borderWidth = 3; // Border width in points (1/72 of an inch)

  doc.setLineWidth(borderWidth);
  doc.setDrawColor(0, 0, 255); // Set the border color to blue (RGB: 0, 0, 255)
  doc.rect(
    borderWidth / 2,
    borderWidth / 2,
    pageWidth - borderWidth,
    pageHeight - borderWidth
  );

  // Generate the table
  doc.autoTable({
    head: headers,
    body: formattedData,
    margin: {
      top: 10 + borderWidth,
      left: 10 + borderWidth,
      right: 10 + borderWidth,
      bottom: 10 + borderWidth,
    },
  });
  const title = employeePdfDownloadTitle;
  const titleFontSize = 18;
  const titleYPosition = 10; // Y position for the title text, accounting for the border width
  const titleXPosition = pageWidth / 2;
  doc.setFontSize(titleFontSize);
  doc.text(title, titleXPosition, titleYPosition, { align: 'center' });

  // Save the PDF
  doc.save('EmployeeDetails.pdf');
};
