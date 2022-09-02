function isEmpty(obj: any) {
  if (!obj) return true;
  if (typeof obj !== 'object') {
    return !!obj;
  }
  return Object.entries(obj).reduce(
    (res, cur) => {
      if (res) {
        return res;
      }
      if (typeof cur === 'object') {
        return res || isEmpty(cur);
      }
      return res || !cur;
    },
    [false]
  );
}

export default isEmpty;
