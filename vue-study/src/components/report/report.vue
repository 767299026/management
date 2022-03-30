<template>
  <div id="report">
    <!--面包屑导航栏-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>数据统计</el-breadcrumb-item>
      <el-breadcrumb-item>数据报表</el-breadcrumb-item>
    </el-breadcrumb>
    <!--卡片视图区域-->
    <el-card>
      <div id="main" style="width: 750px;height: 400px">

      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import _ from 'lodash'

export default {
  name: "report",
  created() {

  },
  data() {
    return {
      options: {
        title: {
          text: '用户来源'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#E9EEF3'
            }
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            boundaryGap: false
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ]
      }
    }
  },
  methods() {

  },
  mounted() {
    const chart = echarts.init(document.getElementById("main"));
    const option = {
      "legend": {
        "data": [
          "华东",
          "华南",
          "华北",
          "西部",
          "其他"
        ]
      },
      "yAxis": [
        {
          "type": "value"
        }
      ],
      "xAxis": [
        {
          "data": [
            "2017-12-27",
            "2017-12-28",
            "2017-12-29",
            "2017-12-30",
            "2017-12-31",
            "2018-1-1"
          ]
        }
      ],
      "series": [
        {
          "name": "华东",
          "type": "line",
          "stack": "总量",
          "areaStyle": {
            "normal": {}
          },
          "data": [
            2999,
            3111,
            4100,
            3565,
            3528,
            6000
          ]
        },
        {
          "name": "华南",
          "type": "line",
          "stack": "总量",
          "areaStyle": {
            "normal": {}
          },
          "data": [
            5090,
            2500,
            3400,
            6000,
            6400,
            7800
          ]
        },
        {
          "name": "华北",
          "type": "line",
          "stack": "总量",
          "areaStyle": {
            "normal": {}
          },
          "data": [
            6888,
            4000,
            8010,
            12321,
            13928,
            12984
          ]
        },
        {
          "name": "西部",
          "type": "line",
          "stack": "总量",
          "areaStyle": {
            "normal": {}
          },
          "data": [
            9991,
            4130,
            7777,
            12903,
            13098,
            14028
          ]
        },
        {
          "name": "其他",
          "type": "line",
          "stack": "总量",
          "areaStyle": {
            "normal": {}
          },
          "data": [
            15212,
            5800,
            10241,
            14821,
            15982,
            14091
          ]
        }
      ]
    }
    const result = _.merge(option,this.options)
    chart.setOption(result)
  }
}
</script>

<style scoped>

</style>