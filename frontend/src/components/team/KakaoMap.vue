<template>
    <div id="map"></div>
</template>

<script>
export default {
    name: "KaKaoMap",
    data() {
        return {
            map: null,
        };
    },
    mounted() {
        if (window.kakao && window.kakao.maps) {
        // 카카오 객체가 있고, 카카오 맵그릴 준비가 되어 있다면 맵 실행
            this.loadMap();
        } else {
            // 없다면 카카오 스크립트 추가 후 맵 실행
            this.loadScript();
        }
    },
    methods: {
        // api 불러오기
        loadScript() {
        const script = document.createElement("script");
        script.src =
            "//dapi.kakao.com/v2/maps/sdk.js?appkey=174d633fdbdf8cc840d5ab1909104e05&autoload=false"; // &autoload=false api를 로드한 후 맵을 그리는 함수가 실행되도록 구현
        script.onload = () => window.kakao.maps.load(this.loadMap); // 스크립트 로드가 끝나면 지도를 실행될 준비가 되어 있다면 지도가 실행되도록 구현

        document.head.appendChild(script); // html>head 안에 스크립트 소스를 추가
        },
        loadMap() {
            const container = document.getElementById("map"); // 지도를 담을 DOM 영역

            // EPSG:5181 좌표를 WGS84로 변환
            const coords = new window.kakao.maps.Coords(497180.0, 1131209.0);
            const wgs84Coords = coords.toLatLng(); // WGS84 좌표로 변환

            const options = {
                // 지도를 생성할 때 필요한 기본 옵션
                center: wgs84Coords, // 변환된 WGS84 좌표 사용
                level: 4, // 지도의 레벨(확대, 축소 정도)
            };

            this.map = new window.kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴
            this.loadMaker(wgs84Coords); // 마커 위치 지정
        },
        // 지정한 위치에 마커 불러오기
        loadMaker(coords) {
        const markerPosition = coords; // 변환된 좌표로 마커 위치 지정

        const marker = new window.kakao.maps.Marker({
            position: markerPosition,
        });

        marker.setMap(this.map);
        },
    }
}
</script>

<style scoped>
#map {
    width: 800px;
    height: 400px;
}
</style>