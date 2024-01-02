<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "@/uni_modules/uview-ui/index.scss";
</style>
<script>
	export default {
		onLaunch: function(params) {
			console.log('App Launch');
			console.log(params)
			if(uni.getStorageSync("userid")){
				var uid = uni.getStorageSync("userid")
			}
			uni.clearStorage();
			uni.setStorageSync("userid",uid)
			if (params.query.vectorid) {
				console.log(params.query.vectorid)
				uni.setStorageSync("vectorid", params.query.vectorid);
				uni.switchTab({
					url:"pages/home/home"  
				})
			}
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/* 	每个页面公共css */
	page {
		height: 100%;
		font: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;

	}
</style>
