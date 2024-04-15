<script>

import {AppEvent, Key, MessageType} from "@/domain/Enums";

const LINE_BREAK_CHAR = "\n";
const TAB_CHAR = "\t";
const POS_CHAR = "$";

export default {
    name: "Coder",
    props: {
        sourceCode: {},
        selectedLines: {},
        smaRunning: Boolean
    },
    data() {
        return {
            lineQuantity: 1,
            impreciseMindinspectorHightWarned: false
        }
    },
    watch: {
        sourceCode() {
            this.loadSourceCode();
        }
    },
    mounted() {
        this.loadSourceCode();

        this.$refs.coder.addEventListener("keyup", (event) => {
            let currentSourceCode = event.target.value;
            this.$emit("sourceCode", currentSourceCode);


            if (!this.impreciseMindinspectorHightWarned && this.smaRunning) {
                this.$emit(AppEvent.MESSAGE,
                 {content: "Editing source code while SMA runs causes Mind Inspector highlighting to be inaccurate",
                     type: MessageType.WARNING, timeout: 10000});
                this.impreciseMindinspectorHightWarned = true;
            }
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
        loadSourceCode() {
            this.$refs.coder.value = this.sourceCode;
            if (this.sourceCode === '') {
                this.lineQuantity = 1;
            } else {
                this.lineQuantity = this.sourceCode.split(LINE_BREAK_CHAR).length;
            }
            this.$refs.coder.style.height = ((this.lineQuantity * 18) + 40) + "px";
        },
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
            <div
                    v-for="index in lineQuantity"
                    :key="index"
                    ref="coderLine"
                    :class="['coder__line', index >= selectedLines.beginLine && index <= selectedLines.endLine ?
                     'selected' : '']"
            >
                <div class="coder__line__selected"
                     v-if="index >= selectedLines.beginLine && index <= selectedLines.endLine"></div>
                {{ index }}
            </div>
        </div>
        <textarea class="coder__text" ref="coder" spellcheck="false"></textarea>
    </div>
</template>

<style scoped>

.coder {
    font-family: 'JetBrains Mono', monospace;
    border-left: 1px solid var(--pallete-color-black-1);
    flex-basis: 0;
    @apply flex overflow-y-auto flex-grow select-none relative;
}

.coder__lines {
    min-width: 68px;
    background-color: var(--pallete-color-black-2);
    color: var(--pallete-text-aside);
    @apply min-h-full h-fit flex flex-col py-5;
}

.coder__text {
    background-color: var(--pallete-color-black-2);
    color: var(--pallete-text-main);
    font-size: 14px;
    line-height: 18px;
    box-sizing: border-box;
    white-space: nowrap;
    @apply min-h-full w-full border-none resize-none overflow-y-hidden p-5;
}

.coder__line {
    height: 18px;
    font-size: 14px;
    line-height: 18px;
    background-color: var(--pallete-color-black-2);
    @apply pl-5;
}

.coder__line__selected {
    height: 18px;
    animation: selecting 0.5s;
    left: 0;
    @apply w-full absolute;
}

@keyframes selecting {
    0% {
        background-color: rgb(255, 255, 255, 0);
    }

    50% {
        background-color: rgb(255, 255, 255, 0.1);
    }

    100% {
        background-color: rgb(255, 255, 255, 0);
    }
}

</style>