import Vue from 'vue'
const DRAFT = 1
const PUBLISH = 2
const DEAL = 3
const END = 4
const RANGE = 300
const Thu = "-thumbnail"
function datePrase(time,flag) {
	var date = new Date(time)
	// console.log(date)
	var Y = date.getFullYear() + '-'
	var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
	var D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' '
	var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
	var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'
	var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds())
	if(flag){
		return Y + M + D + h + m + s
	}else{
		return Y + M + D
	}
}

// function getResultFromDB(dbres) {
// 	var result = []
// 	var temp = dbres.split(";")
// 	temp.pop()
// 	for (let i = 0; i < temp.length; i++) {
// 		var res = {
// 			one: [],
// 			more: []
// 		}
// 		var one = temp[i].split(":")
// 		res.one.push(parseInt(one[0]) - 1);
// 		one[1] = one[1].split(",")
// 		for (let j = 0; j < one[1].length; j++) {
// 			res.more.push(parseInt(one[1][j]) - 1)
// 		}
// 		result.push(res)
// 	}
// 	return result;
// }
function getResultFromDB(dbres) {
	var result = []
	var temp = dbres.split(";")
	temp.pop()
	for (let i = 0; i < temp.length; i++) {
		var res = {
			left: [],
			right: []
		}
		var one = temp[i].split(":")
		one[0] = one[0].split(",")
		for(let k = 0;k<one[0].length;k++){
			res.left.push(parseInt(one[0][k]) - 1);
		}
		one[1] = one[1].split(",")
		for (let j = 0; j < one[1].length; j++) {
			res.right.push(parseInt(one[1][j]) - 1)
		}
		result.push(res)
	}
	return result;
}

function formatResult(wl, refer) {
	var temp = wl.split(";")
	var nw_temp = temp[0].split(":")[1].split(",");
	var nw = []
	var nl = []
	var ref = []
	for (var i = 0; i < nw_temp.length; i++) {
		nw.push(nw_temp[i] - 1);
	}
	var nl_temp = temp[1].split(":")[1].split(",");
	for (var i = 0; i < nl_temp.length; i++) {
		nl.push(nl_temp[i] - 1);
	}
	var ref_temp = refer.split(",");
	for (var i = 0; i < ref_temp.length; i++) {
		var r = {
			can: ref_temp[i].split("=")[0] - 1,
			val: ref_temp[i].split("=")[1]
		}
		ref.push(r);
	}
	var cal_down = {
		NW: nw,
		NL: nl,
		REF: ref
	}
	console.log(cal_down)
	return cal_down;
}

function getuuid(){
	let uuid = "";
	return new Promise((reserve, reject) => { 
		uni.request({
			url:Vue.prototype.urlhead+"/util/getuuid",
			success(res){
				console.log(res)
				uuid = res.data
				reserve(uuid)
			},
			fail(err){
				reserve(uuid)
			}
		})
	})
}

function getLocation() {
	let location = {
		lat: 0,
		lng: 0,
	}
	return new Promise((reserve, reject) => { //因为获取位置是异步接口所以需要使用promise
		uni.getLocation({
			success(res) {
				console.log(res)
				location.lat = res.latitude
				location.lng = res.longitude,
				reserve(location);
			},
			fail(err) {
				reject(location); //获取失败则返回经纬坐标为0
			}
		})
	})
}

// function distance_1(lat1, lng1) {
// 	var that = this;
// 	console.log('计算地点经纬度：', lat1, lng1);
// 	this.getLocation().then((res) => {
// 		let lat2 = res.lat;
// 		let lng2 = res.lng;
// 		let rad1 = lat1 * Math.PI / 180.0;
// 		let rad2 = lat2 * Math.PI / 180.0;
// 		let a = rad1 - rad2;
// 		let b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;

// 		let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(
// 			rad2) * Math.pow(
// 			Math.sin(b / 2), 2)));
// 		s = s * 6378.137;
// 		s = Math.round(s * 10000) / 10000;
// 		s = s.toString();
// 		s = s.substring(0, s.indexOf('.') + 2);
// 		console.log('距离：', s);
// 		if(s>RANGE){
// 			return false;
// 		}else{
// 			return true;
// 		}
// 	});
// }
function distance(lat1,lng1,lat2,lng2){
    lat1 = lat1 || 0;
    lng1 = lng1 || 0;
    lat2 = lat2 || 0;
    lng2 = lng2 || 0;

    var rad1 = lat1 * Math.PI / 180.0;
    var rad2 = lat2 * Math.PI / 180.0;
    var a = rad1 - rad2;
    var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
	var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(rad1) * Math.cos(rad2) * Math.pow(Math.sin(b / 2), 2)));
	s = s * 6378.137;
	s = Math.round(s * 10000) / 10000;
    console.log("距离"+s)
    if(s>RANGE){
		return false;
	}else{
		return true;
	}
}

module.exports = {
	getuuid:getuuid,
	distance: distance,
	getLocation: getLocation,
	formatResult: formatResult,
	getResultFromDB: getResultFromDB,
	dateParse: datePrase,
	DRAFT: DRAFT,
	PUBLISH: PUBLISH,
	DEAL: DEAL,
	END: END,
	RANGE:RANGE,
	Thu:Thu
}
