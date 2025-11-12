// Dynamically creates the profile picture with the initial of the firstname and a random colour as background
export function createProfilPicture(firstName: string): string {
  const letter = firstName.charAt(0).toUpperCase() || '?';
  const colors: string[] = [
    '#D32F2F',
    '#8b000080',
    '#7B1FA2',
    '#303F9F',
    '#1976D2',
    '#0288D1',
    '#0097A7',
    '#00796B',
    '#388E3C',
    '#689F38',
    '#AFB42B',
    '#FFA000',
    '#F57C00',
    '#E64A19',
    '#5D4037',
    '#455A64',
  ]
  const charCode = letter.charCodeAt(0);
  const colorIndex = charCode % colors.length;
  const color: string = colors[colorIndex];

  const canvas = document.createElement('canvas');
  const size: number = 35;
  canvas.width = size;
  canvas.height = size;
  const context = canvas.getContext('2d');
  if (!context) return '';

  context.fillStyle = color;
  context.fillRect(0,0, size, size);

  context.fillStyle = "#ffffff";
  context.font = '20px Arial';
  context.textAlign = 'center';
  context.textBaseline = 'middle';
  context.fillText(letter, size / 2, size / 1.9);

 return canvas.toDataURL('image/png');
}
