import { useToggleStore } from "@/stores/toggleStore";
import type { DirectiveBinding } from "vue";

export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const toggleStore = useToggleStore();

    const handler = (event: MouseEvent) => {
      const target = event.target as Node;

      // Only trigger when the click is outside the element
      if (el && !el.contains(target)) {
        binding.value(event);
      }
    };

    // Timeout to prevent the click that opened the dropdown in the first place to trigger immediately the onClickOutside toggler.
    // since the first click (to open the dropdown) is technically outside the dropdown element.
    setTimeout(() => {
      document.addEventListener("click", handler);
    });

    // Store the handler for later removal
    (el as any).__clickOutsideHandler__ = handler;
  },

  unmounted(el: HTMLElement) {
    const handler = (el as any).__clickOutsideHandler__;
    if (handler) {
      document.removeEventListener("click", handler);
      delete (el as any).__clickOutsideHandler__;
    }
  }
};

