<script>

import {Key} from "@/domain/Enums";

const LINE_BREAK_CHAR = "\n";
const TAB_CHAR = "\t";
const POS_CHAR = "$";

export default {
    name: "Coder",
    props: {
        sourceCode: {}
    },
    data() {
      return {
          lineQuantity: 1
      }
    },
    watch: {
        sourceCode() {
            this.$refs.coder.value = this.sourceCode;
            if (this.sourceCode === '') {
                this.lineQuantity = 1;
            } else {
                this.lineQuantity = this.sourceCode.split(LINE_BREAK_CHAR).length;
            }
            this.$refs.coder.style.height = ((this.lineQuantity * 14) + 40) + "px";
        }
    },
    mounted() {
        this.$refs.coder.addEventListener("keyup", (event) => {
            let currentSourceCode = event.target.value;
            this.$emit("sourceCode", currentSourceCode);
        });

        this.$refs.coder.addEventListener("keydown", (event) => {
            if (event.key === Key.BRACKET_RIGHT) {
                this.writeAction(event, `{${POS_CHAR}}`, true);
            } else if (event.key === Key.SQUARE_BRACKET_RIGHT) {
                this.writeAction(event, `[${POS_CHAR}]`, true);
            } else if (event.key === Key.PARENTESIS_RIGHT) {
                this.writeAction(event, `(${POS_CHAR})`, true);
            } else if (event.key === Key.TAB) {
                event.preventDefault();
                this.writeAction(event, TAB_CHAR, false);
            } else if (event.key === Key.DOUBLE_BACKQUOTE) {
                this.writeAction(event, `"${POS_CHAR}"`, true);
            } else if (event.key === Key.BACKQUOTE) {
                this.writeAction(event, `'${POS_CHAR}'`, true);
            }
        });
    },
    methods: {
        writeAction(event, text, setPositionInner) {
            event.preventDefault();
            setTimeout(() => {
                let selectionStart = this.$refs.coder.selectionStart;
                let beforeText = this.$refs.coder.value.substring(0, selectionStart);
                let afterText = this.$refs.coder.value.substring(selectionStart);
                this.$refs.coder.value = beforeText + text + afterText;
                let selectionPosition;

                if (setPositionInner) {
                    selectionPosition = this.$refs.coder.value.indexOf(POS_CHAR);
                    this.$refs.coder.value = this.$refs.coder.value.replace(POS_CHAR, "");
                } else {
                    selectionPosition = selectionStart + text.length;
                }
                setTimeout(() => {
                    this.$refs.coder.setSelectionRange(selectionPosition, selectionPosition);
                }, 0)
            }, 0);
        },
    }
}
</script>

<template>
    <div class="coder">
        <div class="coder__lines" ref="coderLines">
            <div v-for="index in lineQuantity" :key="index" class="coder__line pl-5">{{ index }}</div>
        </div>
        <textarea class="coder__text" ref="coder" spellcheck="false"></textarea>
    </div>
</template>

<style scoped>

.coder {
    font-family: 'JetBrains Mono', monospace;
    background-color: var(--pallete-color-black-1);
    border-left: 1px solid var(--pallete-color-black-1);
    flex-basis: 100px;
    @apply flex overflow-y-scroll flex-grow;
}

.coder__lines {
    min-width: 68px;
    background-color: var(--pallete-color-black-2);
    color: var(--pallete-text-aside);
    border-right: 5px solid var(--pallete-color-black-3);
    @apply min-h-full h-fit flex flex-col py-5;
}

.coder__text {
    background-color: var(--pallete-color-black-1);
    color: var(--pallete-text-main);
    font-size: 14px;
    line-height: 1;
    box-sizing: border-box;
    @apply min-h-full w-full border-none resize-none overflow-y-hidden p-5;
}

.coder__line {
    height: 14px;
    line-height: 1;
    font-size: 14px;
}

</style>