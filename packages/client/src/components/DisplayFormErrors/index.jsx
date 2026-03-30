export const DisplayFormErrorMessages = function (msg) {
  if (msg) {
    if (typeof msg === 'object') {
      if (msg.length === 1) {
        return msg[0];
      }
      return msg[0].substring(0, msg[0].indexOf('.'));
    }
  }
  return msg;
};
