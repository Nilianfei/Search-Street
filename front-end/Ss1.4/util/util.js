function formatTime(time) {
  if (typeof time !== 'number' || time < 0) {
    return time
  }

  var hour = parseInt(time / 3600)
  time = time % 3600
  var minute = parseInt(time / 60)
  time = time % 60
  var second = time

  return ([hour, minute, second]).map(function (n) {
    n = n.toString()
    return n[1] ? n : '0' + n
  }).join(':')
}
/*
 * 时间戳转换为yyyy-MM-dd hh:mm:ss 格式  formatDate()
 * inputTime   时间戳
 */
function formatDate(inputTime) {
  var strlist=inputTime.split(",");
  var y = strlist[0].substr(1,4);
  var m = strlist[1];
  m = m < 10 ? ('0' + m) : m;
  var d = strlist[2];
  d = d < 10 ? ('0' + d) : d;
  var h = strlist[3];
  h = h < 10 ? ('0' + h) : h;
  var minute = strlist[4];
  var second = strlist[5].substr(0, strlist[5].length-1);
  minute = minute < 10 ? ('0' + minute) : minute;
  second = second < 10 ? ('0' + second) : second;
  return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
}

function formatDate1(time) {

  var datetime = new Date();
  datetime.setTime(time);
  var year = datetime.getFullYear();
  var month = datetime.getMonth() + 1;
  var date = datetime.getDate();
  var hour = datetime.getHours();
  if(month<=9){
    month="0"+month;
  }
  if(date<=9){
    date="0"+date;
  }
  if (hour <= 9) {
    hour = "0" + hour;
  }
  var minute = datetime.getMinutes();
  if (minute <= 9) {
    minute = "0" + minute;
  }
  var second = datetime.getSeconds();
  if (second <= 9) {
    second = "0" + second;
  }
  // var mseconds = datetime.getMilliseconds();
  return  month + "-" + date + " " + hour + ":" + minute + ":" + second;//+"."+mseconds;
}

function formatLocation(longitude, latitude) {
  if (typeof longitude === 'string' && typeof latitude === 'string') {
    longitude = parseFloat(longitude)
    latitude = parseFloat(latitude)
  }

  longitude = longitude.toFixed(2)
  latitude = latitude.toFixed(2)

  return {
    longitude: longitude.toString().split('.'),
    latitude: latitude.toString().split('.')
  }
}

module.exports = {
  formatTime: formatTime,
  formatDate: formatDate,
  formatLocation: formatLocation ,
  formatDate1:formatDate1

}