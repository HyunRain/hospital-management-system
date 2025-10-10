import { ref } from 'vue';

export function useErrorAlert(hideTimerMs = 10000) {
  const showError = ref(false)
  const errorMessage = ref('')
  const errorAlertKey = ref(Date.now())
  let timer: ReturnType<typeof setTimeout>

  function triggerBackendError(message: string) {
    window.clearTimeout(timer)
    showError.value = true
    errorAlertKey.value = Date.now()
    errorMessage.value = message
  
    timer = setTimeout(() => {
      showError.value = false
      errorMessage.value = ''
    }, hideTimerMs)
  }
  return {
    showError,
    errorMessage,
    errorAlertKey,
    triggerBackendError
  };
}
