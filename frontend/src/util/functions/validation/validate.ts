import { validateField } from "./validateField";
import type { ValidationResult } from '@/util/types/types'

export function validate(
  input: Record<string, string>,
  rules: Record<string, Array<(value: string) => boolean | string>>,
): ValidationResult {
  for (const keyInput in input) {
    for (const keyRule in rules) {
      if(keyInput === keyRule) {
        for(const keyFunction of rules[keyRule]) {
          const result = validateField(keyInput, input[keyInput], keyFunction);
          if(!result.success) {
            return result;
          }
        }
      }
    }
  }

  return  { success: true };
}
