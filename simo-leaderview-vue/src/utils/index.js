
/**
 * 单词首字母大写
 * 例: hello 转 Hello
 * @param {*} str 
 */
export function capitalize (str) {
    return str.slice(0, 1).toUpperCase() + str.slice(1);
  };

/**
 * 
 * @param {Blob} data 
 * @return {download file}
 */
export function download (name, data) {
  let link = document.createElement('a');
  link.style.display = 'none';
  link.href = window.URL.createObjectURL(new Blob([data]));
  link.setAttribute('download', name);
  document.body.appendChild(link);
  link.click();
  // remove
  window.URL.revokeObjectURL(link.href);
  document.body.removeChild(link);
}

export default undefined;