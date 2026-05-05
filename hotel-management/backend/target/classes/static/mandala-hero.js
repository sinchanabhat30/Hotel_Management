// Mandala hero animation for pixel-perfect landing
const mandalaImg = document.getElementById('mandala-img');
const foodImages = [
  'https://images.unsplash.com/photo-1668236543090-82eba5ee5976?auto=format&fit=crop&w=700&q=80',
  'https://images.unsplash.com/photo-1589647363585-f4a7d3877b10?auto=format&fit=crop&w=700&q=80',
  'https://images.unsplash.com/photo-1559054660-8b5f7c0e3c2f?auto=format&fit=crop&w=700&q=80'
];
let current = 0;
let started = false;

function showImage(idx, fadeIn = true) {
  mandalaImg.setAttribute('href', foodImages[idx]);
  mandalaImg.setAttribute('xlink:href', foodImages[idx]);
  mandalaImg.style.opacity = fadeIn ? '1' : '0';
}

// Step 1: Mandala is solid dark (no image)
showImage(0, false);

// Step 2: After 1s, fade in first image
setTimeout(() => {
  showImage(0, true);
  started = true;
  // Step 3: Start slideshow after 2s
  setTimeout(() => {
    setInterval(() => {
      current = (current + 1) % foodImages.length;
      mandalaImg.style.opacity = '0';
      setTimeout(() => {
        showImage(current, true);
      }, 600);
    }, 2500);
  }, 1000);
}, 1000);
