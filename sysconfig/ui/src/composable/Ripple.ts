const mouseDownRipple = (event: MouseEvent, element: HTMLElement) => {
    if (event.button == 2) {
        return null;
    }
    const elementRect = element.getBoundingClientRect();
    const x = event.clientX - elementRect.left;
    const y = event.clientY - elementRect.top;

    const ripple = document.createElement("div");
    ripple.classList.add("ripple");
    ripple.style.top = `${y}px`;
    ripple.style.left = `${x}px`;
    ripple.style.cursor = window.getComputedStyle(element).cursor;

    element.appendChild(ripple);

    const elementPosition = window.getComputedStyle(element).position;
    if (elementPosition == "absolute") {
        element.style.position = "fixed";
    } else if (elementPosition == "static") {
        element.style.position = "relative";
    }
    element.style.overflow = "hidden";

    let ratio = 0;
    if (element.clientWidth > element.clientHeight) {
        ratio = element.clientWidth * 2;
    } else {
        ratio = element.clientHeight * 2;
    }
    ratio += 20;
    setTimeout(() => {
        ripple.style.height = `${ratio}px`;
    }, 0);
    return ripple;
}

const mouseUpRipple = (ripple: HTMLElement, element: HTMLElement) => {
    if (ripple != null) {
        setTimeout(() => {
            ripple.style.opacity = "0";
            setTimeout(() => {
                ripple.remove();
                if(!element.querySelector(".ripple")) {
                    element.style.removeProperty("position");
                }
            }, 300);
        }, 100);
    }
}

export function useRipple(element: HTMLElement) {
    let ripple: any;
    element.addEventListener("mousedown", (event) => ripple = mouseDownRipple(event, element));
    element.addEventListener("mouseup", (event) => mouseUpRipple(ripple, element));
    document.body.addEventListener("mouseup", (event) => mouseUpRipple(ripple, element));
}

export function removeRipple(element: HTMLElement) {
    let ripple: any;
    const elementDefaultPosition = window.getComputedStyle(element).position
    element.removeEventListener("mousedown", (event) => ripple = mouseDownRipple(event, element));
    element.removeEventListener("mouseup", (event) => mouseUpRipple(ripple, element));
    document.body.removeEventListener("mouseup", (event) => mouseUpRipple(ripple, element));
}