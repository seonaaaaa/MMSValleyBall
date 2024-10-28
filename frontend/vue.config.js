const { defineConfig } = require('@vue/cli-service');
const webpack = require('webpack'); // VUE_PROD_HYDRATION_MISMATCH_DETAILS 플래그를 추가

module.exports = defineConfig({
  transpileDependencies: true,
  // 이 플래그는 Vue의 하이드레이션 관련 디버그 정보를 제공하는 용도로, 
  // 빌드 과정에서 플래그가 인식되면 해당 경고를 출력하지 않게 됨
  // 실제로 확인하려면 빌드하고, 빌드 결과물에서 콘솔 경고가 출력되는지 확인 필요
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(true) // 트리 쉐이킹 방지 목적의 true 설정
      })
    ]
  }
})
