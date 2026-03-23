import { Link } from 'react-router-dom';

import errorBot from 'assets/images/error-bot.webp';

const ErrorPage = () => {
  return (
    <div className="error-page">
      <img className="error-page-bot" src={errorBot} alt="" />
      <Link className="error-page-btn" to="/">
        Back To Homepage
      </Link>
    </div>
  );
};

export default ErrorPage;
