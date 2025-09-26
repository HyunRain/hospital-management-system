import type { ValidationResult } from '@/util/types/types';

export function validateField( fieldName: string, value: string, validatorFn: Function ): ValidationResult {
  const result = validatorFn(value)

  if (typeof result === 'string' && result === 'required.') {
    return {
      success: false,
      field: fieldName,
      validator: validatorFn.name,
      error: fieldName.charAt(0).toUpperCase() + fieldName.slice(1) + ' ' + result,
    }
  } else if (typeof result === 'string') {
    return {
      success: false,
      field: fieldName,
      validator: validatorFn.name,
      error: result,
    }
  }

  return { success: true }
}
