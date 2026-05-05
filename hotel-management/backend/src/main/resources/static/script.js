const API_BASE = "";

// Room availability counts (persisted in localStorage)
const ROOM_COUNTS_KEY = "dakshinViharRoomCounts";
const INITIAL_ROOM_COUNT = 10;

function getRoomCounts() {
  const stored = localStorage.getItem(ROOM_COUNTS_KEY);
  if (stored) {
    return JSON.parse(stored);
  }
  // Initialize with default counts
  const counts = {
    "AC Deluxe": INITIAL_ROOM_COUNT,
    "Non-AC Deluxe": INITIAL_ROOM_COUNT,
    "Family Suite": INITIAL_ROOM_COUNT,
    "Executive Double": INITIAL_ROOM_COUNT
  };
  localStorage.setItem(ROOM_COUNTS_KEY, JSON.stringify(counts));
  return counts;
}

function updateRoomCount(roomType, change) {
  const counts = getRoomCounts();
  if (counts[roomType] !== undefined) {
    counts[roomType] = Math.max(0, counts[roomType] + change);
    localStorage.setItem(ROOM_COUNTS_KEY, JSON.stringify(counts));
  }
}

function getRoomAvailability(roomType) {
  const counts = getRoomCounts();
  const count = counts[roomType] || 0;
  if (count === 0) return { text: "Fully Booked", color: "red", available: false };
  if (count <= 3) return { text: `Only ${count} left`, color: "orange", available: true };
  return { text: `${count} available`, color: "green", available: true };
}

function $(id) {
  return document.getElementById(id);
}

function formatMoney(value) {
  const num = Number(value);
  if (Number.isNaN(num)) return String(value ?? "");
  return `₹${num.toFixed(2)}`;
}

function escapeHtml(str) {
  return String(str ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function todayISO() {
  const d = new Date();
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const dd = String(d.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
}

function addDaysISO(days) {
  const d = new Date();
  d.setDate(d.getDate() + days);
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const dd = String(d.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
}

async function fetchJson(url) {
  const res = await fetch(url);
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`Request failed: ${res.status} ${res.statusText}${text ? ` - ${text}` : ""}`);
  }
  return res.json();
}

function createMenuCard(item) {
  const name = escapeHtml(item.name);
  const price = formatMoney(item.price);
  const img = escapeHtml(item.imageUrl);

  const el = document.createElement("div");
  el.className = "menu-card";
  el.innerHTML = `
    <img src="${img}" alt="${name}" loading="lazy" />
    <div class="menu-card-body">
      <div class="menu-name">${name}</div>
      <div class="menu-price">${price}</div>
    </div>
  `;
  return el;
}

function setupMenuCarousel() {
  const viewport = document.querySelector(".carousel-viewport");
  const track = $("menuTrack");
  const prevBtn = $("menuPrev");
  const nextBtn = $("menuNext");
  const cards = Array.from(track.children);

  if (cards.length === 0) return;

  // Step = distance between two neighboring cards.
  const card0 = cards[0];
  const card1 = cards[1] ?? cards[0];
  const step = card1.offsetLeft - card0.offsetLeft || card0.getBoundingClientRect().width + 16;

  function visibleCount() {
    const w = viewport.getBoundingClientRect().width;
    // This intentionally leaves partial visibility (2-2.5 items) on large screens.
    return Math.max(1, Math.round(w / step));
  }

  let menuIndex = 0;

  function render() {
    const v = visibleCount();
    const maxIndex = Math.max(0, cards.length - v);

    if (menuIndex > maxIndex) menuIndex = maxIndex;

    track.style.transform = `translateX(${-menuIndex * step}px)`;
    prevBtn.disabled = menuIndex <= 0;
    nextBtn.disabled = menuIndex >= maxIndex;
  }

  prevBtn.onclick = () => {
    menuIndex = Math.max(0, menuIndex - 1);
    render();
  };

  nextBtn.onclick = () => {
    menuIndex = menuIndex + 1;
    render();
  };

  window.addEventListener("resize", () => render());
  render();
}

function setupCuisineCards() {
  const cards = document.querySelectorAll(".cuisine-card");
  const title = $("cuisineDetailTitle");
  const text = $("cuisineDetailText");
  if (!title || !text || cards.length === 0) return;

  function selectCard(card) {
    cards.forEach((c) => c.classList.toggle("active", c === card));
    title.textContent = card.dataset.title || card.querySelector("h3").textContent;
    text.textContent = card.dataset.description || card.querySelector("p").textContent;
  }

  cards.forEach((card) => {
    card.addEventListener("click", () => selectCard(card));
    card.addEventListener("keydown", (event) => {
      if (event.key === "Enter" || event.key === " ") {
        event.preventDefault();
        selectCard(card);
      }
    });
  });

  selectCard(cards[0]);
}

async function loadMenu() {
  const loading = $("menuLoading");
  const errorBox = $("menuError");
  const track = $("menuTrack");

  // Prevent multiple loads
  if (track.dataset.loaded === "true") return;

  track.innerHTML = "";

  try {
    loading.hidden = false;
    errorBox.hidden = true;

    const menu = await fetchJson(`${API_BASE}/api/menu`);
    if (!Array.isArray(menu)) throw new Error("Menu response was not an array.");

    // Render cards
    track.innerHTML = "";
    for (const item of menu) {
      track.appendChild(createMenuCard(item));
    }

    track.dataset.loaded = "true";
    loading.hidden = true;
    setupMenuCarousel();
  } catch (err) {
    console.error(err);
    loading.hidden = true;
    errorBox.hidden = false;
    errorBox.textContent = "Could not load menu. Please try again later.";
  }
}

function openBookingModal(room) {
  const modal = $("bookingModal");
  const roomTypeInput = $("bookingRoomType");
  const subtitle = $("bookingSubtitle");
  const roomType = room.type;
  const price = formatMoney(room.price);

  roomTypeInput.value = roomType;
  subtitle.textContent = `Room type: ${roomType} - ${price}`;

  const name = $("bookingName");
  const email = $("bookingEmail");
  const checkIn = $("bookingCheckIn");
  const checkOut = $("bookingCheckOut");

  // Pre-fill dates so it works with the current modal UI.
  checkIn.value = todayISO();
  checkOut.value = addDaysISO(1);

  name.value = "";
  email.value = "";

  const status = $("bookingStatus");
  status.textContent = "";

  modal.classList.add("show");
  modal.removeAttribute("hidden");
  modal.hidden = false;
  modal.setAttribute("aria-hidden", "false");
}

function closeBookingModal() {
  const modal = $("bookingModal");
  modal.classList.remove("show");
  modal.setAttribute("aria-hidden", "true");
  modal.hidden = true;
}

async function createBooking(payload) {
  const res = await fetch(`${API_BASE}/api/bookings`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`Booking failed: ${res.status} ${res.statusText}${text ? ` - ${text}` : ""}`);
  }
  return res.json();
}

function setupBookingModalHandlers() {
  const modal = $("bookingModal");
  const overlay = modal.querySelector(".modal-overlay");
  const closeBtn = modal.querySelector(".modal-close");
  const form = $("bookingForm");
  const status = $("bookingStatus");

  overlay.addEventListener("click", closeBookingModal);
  closeBtn.addEventListener("click", closeBookingModal);

  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && modal && modal.hidden === false) closeBookingModal();
  });

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    status.textContent = "Submitting booking...";

    try {
      const payload = {
        customerName: $("bookingName").value,
        email: $("bookingEmail").value,
        roomType: $("bookingRoomType").value,
        checkIn: $("bookingCheckIn").value,
        checkOut: $("bookingCheckOut").value
      };

      await createBooking(payload);
      // Decrement room count after successful booking
      updateRoomCount(payload.roomType, -1);
      status.textContent = "Booking confirmed! We will contact you shortly.";
      await loadRooms(); // Refresh UI to show updated availability
      setTimeout(() => closeBookingModal(), 900);
    } catch (err) {
      console.error(err);
      status.textContent = "Booking failed. Please check the details and try again.";
    }
  });
}

function createRoomCard(room) {
  const el = document.createElement("div");
  el.className = "room-card";
  const img = escapeHtml(room.imageUrl);
  const type = escapeHtml(room.type);
  const price = formatMoney(room.price);

  const availability = getRoomAvailability(type);
  const isAvailable = availability.available;

  el.innerHTML = `
    <img src="${img}" alt="${type}" loading="lazy" />
    <div class="room-card-body">
      <div class="room-type">${type}</div>
      <div class="room-price">${price}</div>
      <div class="room-availability ${availability.color}">${availability.text}</div>
      <div class="room-actions">
        <button class="btn btn-primary" type="button" ${!isAvailable ? "disabled" : ""}>${!isAvailable ? "Fully Booked" : "Book"}</button>
      </div>
    </div>
  `;

  const btn = el.querySelector("button");
  if (isAvailable) {
    btn.addEventListener("click", () => openBookingModal(room));
  }
  return el;
}

function appendChatMessage(text, role) {
  const messages = $("chatMessages");
  const messageEl = document.createElement("div");
  messageEl.className = `chat-message ${role}`;
  messageEl.innerHTML = `<div class="chat-message-bubble">${escapeHtml(text)}</div>`;
  messages.appendChild(messageEl);
  messages.scrollTop = messages.scrollHeight;
}

function showTypingBubble() {
  const messages = $("chatMessages");
  const typing = document.createElement("div");
  typing.className = "chat-message assistant typing";
  typing.innerHTML = `<div class="chat-message-bubble">Typing…</div>`;
  messages.appendChild(typing);
  messages.scrollTop = messages.scrollHeight;
  return typing;
}

async function askAiAssistant(message) {
  const input = $("chatInput");
  const submitButton = document.querySelector(".chat-send");
  const typingBubble = showTypingBubble();

  submitButton.disabled = true;
  input.disabled = true;

  try {
    const response = await fetch(`${API_BASE}/api/ai/chat`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ message })
    });

    if (!response.ok) {
      const text = await response.text().catch(() => "");
      throw new Error(`Chat failed: ${response.status} ${response.statusText}${text ? ` - ${text}` : ""}`);
    }

    const data = await response.json();
    if (typingBubble.parentElement) {
      typingBubble.parentElement.removeChild(typingBubble);
    }

    appendChatMessage(data.reply || "Sorry, I could not answer that right now.", "assistant");
  } catch (err) {
    console.error(err);
    if (typingBubble.parentElement) {
      typingBubble.parentElement.removeChild(typingBubble);
    }
    appendChatMessage("Sorry, our assistant is unavailable. Please try again later.", "assistant");
  } finally {
    submitButton.disabled = false;
    input.disabled = false;
    input.focus();
  }
}

function openChatWindow() {
  const windowEl = $("chatWindow");
  windowEl.classList.remove("hidden");
  windowEl.removeAttribute("hidden");
  windowEl.setAttribute("aria-hidden", "false");
  $("chatInput").focus();
}

function closeChatWindow() {
  const windowEl = $("chatWindow");
  windowEl.classList.add("hidden");
  windowEl.setAttribute("hidden", "true");
  windowEl.setAttribute("aria-hidden", "true");
}

function setupChatWidget() {
  const toggle = $("chatToggle");
  const closeBtn = $("chatClose");
  const form = $("chatForm");

  toggle.addEventListener("click", () => {
    const windowEl = $("chatWindow");
    if (windowEl.classList.contains("hidden")) {
      openChatWindow();
    } else {
      closeChatWindow();
    }
  });

  closeBtn.addEventListener("click", closeChatWindow);

  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const input = $("chatInput");
    const message = input.value.trim();
    if (!message) return;

    appendChatMessage(message, "user");
    input.value = "";
    await askAiAssistant(message);
  });

  document.addEventListener("keydown", (event) => {
    if (event.key === "Escape" && !$("chatWindow").classList.contains("hidden")) {
      closeChatWindow();
    }
  });
}

async function loadRooms() {
  const grid = $("roomsGrid");
  grid.innerHTML = "";

  const rooms = await fetchJson(`${API_BASE}/api/rooms`);
  if (!Array.isArray(rooms)) throw new Error("Rooms response was not an array.");

  const seenTypes = new Set();
  const uniqueRooms = [];

  for (const room of rooms) {
    const typeKey = String(room.type ?? "").trim().toLowerCase();
    if (!typeKey || seenTypes.has(typeKey)) continue;
    seenTypes.add(typeKey);
    uniqueRooms.push(room);
  }

  if (uniqueRooms.length === 0) {
    grid.innerHTML = `<div class="error">No rooms are available right now. Please try again later.</div>`;
    return;
  }

  for (const room of uniqueRooms) {
    grid.appendChild(createRoomCard(room));
  }
}

async function submitContact(payload) {
  const res = await fetch(`${API_BASE}/api/contact`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`Contact failed: ${res.status} ${res.statusText}${text ? ` - ${text}` : ""}`);
  }
  return res.json();
}

function setupContactForm() {
  const form = $("contactForm");
  const status = $("contactStatus");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    status.textContent = "Sending message...";

    try {
      const payload = {
        name: form.name.value,
        email: form.email.value,
        message: form.message.value
      };

      await submitContact(payload);
      status.textContent = "Message sent successfully. Thank you!";
      form.reset();
    } catch (err) {
      console.error(err);
      status.textContent = "Could not send message. Please try again later.";
    }
  });
}

function setupHeroSlider() {
  const slides = document.querySelectorAll(".hero-slide");
  if (slides.length === 0) return;

  let currentIndex = 0;

  function rotateSlide() {
    slides.forEach(slide => slide.classList.remove("active"));
    slides[currentIndex].classList.add("active");
    currentIndex = (currentIndex + 1) % slides.length;
  }

  // Initial setup
  rotateSlide();

  // Rotate every 10 seconds (10s animation + 0s pause)
  setInterval(rotateSlide, 10000);
}

document.addEventListener("DOMContentLoaded", async () => {
  setupHeroSlider();
  setupCuisineCards();

  try {
    await loadMenu();
  } catch (e) {
    // loadMenu already handles user-facing error.
  }

  try {
    await loadRooms();
  } catch (e) {
    console.error(e);
    $("roomsGrid").innerHTML = `<div class="error">Could not load rooms. Please try again later.</div>`;
  }

  setupBookingModalHandlers();
  setupContactForm();
  setupChatWidget();
});

