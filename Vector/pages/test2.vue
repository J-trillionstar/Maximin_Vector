<template>
	<view>
		<button @click="test" type="primary">打开</button>
	</view>
</template>
<script>
	import Vue from 'vue'
	export default {
		data() {
			return {
				cans:{
					img:""
				}
			}
		},
		methods: {
				test(){
				
				uni.request({
					url: Vue.prototype.urlhead+"/vector/creatVec",
					method:"POST",
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: {
						vectorid:"123456789ws"
					},
					success: (res) => {
						console.log(res)
					}
				})
			}
			}
		}
</script>
<style>

</style>
