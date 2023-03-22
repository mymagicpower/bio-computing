<template>
  <div class="app-container">
    <el-form ref="form" :model="form">
      <el-input v-model="form.smiles" placeholder="please enter data" class="input-with-select">
        <el-select slot="prepend" v-model="form.topK" placeholder="Select">
          <el-option label="Top 5" value="5" />
          <el-option label="Top 10" value="10" />
          <el-option label="Top 20" value="20" />
          <el-option label="Top 50" value="50" />
          <el-option label="Top 100" value="100" />
          <el-option label="Top 200" value="200" />
        </el-select>
        <el-button slot="append" icon="el-icon-search" element-loading-text="loading" @click="onSubmit" />

      </el-input>
      <el-table
        :data="form.result"
        stripe
        style="width: 100%">
        <el-table-column
          prop="score"
          label="Score"
          width="100">
        </el-table-column>
        <el-table-column
          label="Mol Image">
          <template width="100" slot-scope="scope">
            <img style="width:100px;height:100px;border:none;" :src="scope.row.url">
          </template>
        </el-table-column>
        <el-table-column
          prop="smiles"
          label="smiles"
          width="600">
        </el-table-column>
      </el-table>
    </el-form>
  </div>
</template>

<script>
import { search } from '@/api/search'

export default {
  name: 'InferenceDetail',
  data() {
    return {
      fullscreenLoading: false,
      form: {
        topK: 5,
        smiles: '',
        result: ''
      }
    }
  },
  methods: {
    onSubmit() {
      this.fullscreenLoading = true
      search(this.form).then(response => {
        this.fullscreenLoading = false
        this.form.result = response.data.result
      })
    }
  }
}
</script>

<style>
  .el-select .el-input {
    width: 130px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>
