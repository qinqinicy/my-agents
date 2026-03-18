<template>
  <div class="attachment-container">
    <div
      class="attachment-item flex justify-between items-center p-[10px] pr-[7px]"
    >
      <div class="attachment-item-left flex-1">
        <div
          class="text-[#2B2B2B] font-medium leading-[20px] text-[14px] font-['MiSans']"
        >
          {{ temp.fileName }}
        </div>
        <div
          class="mt-[8px] text-[#818181] font-normal leading-[15px] text-[12px] font-['MiSans']"
        ></div>
      </div>
      <div class="attachment-item-right ml-[5px]">
        <img
          class="w-[40px] h-[45px]"
          referrerpolicy="no-referrer"
          :src="iconMap[temp.fileSuffix]"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import attachment_doc from "@/assets/images/attachment_doc.png";
import attachment_pdf from "@/assets/images/attachment_pdf.png";
import attachment_pptx from "@/assets/images/attachment_pptx.png";
import attachment_xlsx from "@/assets/images/attachment_xlsx.png";
const { temp } = defineProps({
  temp: {
    type: Object,
    default: () => {
      return {};
    }
  }
});
const iconMap = {
  pdf: attachment_pdf,
  docx: attachment_doc,
  ppt: attachment_pptx,
  xlsx: attachment_xlsx,
  png:
    import.meta.env.VITE_PUBLIC_PATH + "/decisfile/files" + temp.currentFileUrl,
  jpg:
    import.meta.env.VITE_PUBLIC_PATH + "/decisfile/files" + temp.currentFileUrl,
  jpeg:
    import.meta.env.VITE_PUBLIC_PATH + "/decisfile/files" + temp.currentFileUrl
};
</script>

<style lang="less" scoped>
.attachment-container {
  background: linear-gradient(
      180deg,
      rgba(231, 242, 243, 0.47) 0%,
      #ffffff 100%
    ),
    linear-gradient(
      271deg,
      rgba(243, 253, 249, 0.68) 0%,
      rgba(233, 255, 245, 0) 100%
    );
  box-shadow: 2px 2px 4px 1px rgba(185, 201, 201, 0.16);
  border-radius: 8px;
  border: 1px solid;
  border-image: linear-gradient(
      215deg,
      rgba(255, 255, 255, 0),
      rgba(255, 255, 255, 1)
    )
    1 1;
}
</style>
