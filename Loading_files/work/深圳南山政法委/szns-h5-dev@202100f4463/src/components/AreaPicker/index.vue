<script setup>
import { receiveDpt } from "@/api/eventDetails";
import { isArray } from "@/utils/validate";

const showPicker = defineModel("show");
const customFieldName = {
  text: "departmentName",
  value: "departmentCode"
};
const columns = ref([]);
const getReceiveDpt = async () => {
  try {
    const res = await receiveDpt();
    if (
      isArray(res.result.districtList) &&
      res.result.districtList.length > 0
    ) {
      columns.value = [...res.result.districtList];
      emit("confirm", [res.result.districtList[0]]);
    } else {
      columns.value = [
        {
          departmentName: "南山区",
          departmentCode: "440305"
        }
      ];
      emit("confirm", [
        {
          departmentName: "南山区",
          departmentCode: "440305"
        }
      ]);
    }
  } catch {
    columns.value = [
      {
        departmentName: "南山区",
        departmentCode: "440305"
      }
    ];
    emit("confirm", [
      {
        departmentName: "南山区",
        departmentCode: "440305"
      }
    ]);
  }
};
const emit = defineEmits(["cancel", "update:showValue", "confirm", "init"]);
const onCancel = () => {
  showPicker.value = false;
  emit("cancel");
};
const onConfirm = ({ selectedOptions }) => {
  showPicker.value = false;
  emit("confirm", selectedOptions);
};
onMounted(() => {
  getReceiveDpt();
});
</script>

<template>
  <div>
    <van-popup v-model:show="showPicker" round position="bottom">
      <van-picker
        :columns="columns"
        :columns-field-names="customFieldName"
        @cancel="onCancel"
        @confirm="onConfirm"
      />
    </van-popup>
  </div>
</template>

<style scoped></style>
