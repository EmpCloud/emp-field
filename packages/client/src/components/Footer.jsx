const Footer = () => {
  return (
    <footer>
      <a
        href="https://empmonitor.com/privacy-policy/"
        target="_blank"
        rel="noopener noreferrer">
        Privacy Hub
      </a>
      &nbsp;|&nbsp;
      <a
        href="https://empmonitor.com/terms-and-conditions/"
        target="_blank"
        rel="noopener noreferrer">
        T & C
      </a>
      &nbsp;|&nbsp; EmpMonitor. © {new Date().getFullYear() ?? ''} All Rights
      Reserved
    </footer>
  );
};

export default Footer;
