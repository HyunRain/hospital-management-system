export function required(value: string) {
  return !!value || 'required.'
}

export function validEmail(value: string) {
  const pattern = /^[\w.!#$%&'*+/=?^`{|}~-]+@[a-z\d](?:[a-z\d-]{0,61}[a-z\d])?(?:\.[a-z\d](?:[a-z\d-]{0,61}[a-z\d])?)*$/i
  return pattern.test(value) || 'Invalid email.'
}
