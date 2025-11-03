export function useEnumByKeyByValue() {
  function getEnumKeyByValue<T extends Record<string, string>>(enumObj: T, value: string) {
    return Object.keys(enumObj).find((key) => enumObj[key] === value);
  }

  return { getEnumKeyByValue };
}
