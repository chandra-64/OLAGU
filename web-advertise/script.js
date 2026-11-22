// OLAGU On-Device Intelligence — Mobile Interface Animation & Interaction

document.addEventListener('DOMContentLoaded', () => {
  initNewsTicker();
  initMobilePhoneAnimation();
  initPhoneCursorTracking();
});

function initNewsTicker() {
  const container = document.querySelector('.ticker-track-container');
  if (!container) return;

  const templateItem = container.querySelector('.ticker-content');
  if (!templateItem) return;

  // Clear container to assemble duplicate tracks programmatically
  container.innerHTML = '';

  const track1 = document.createElement('div');
  track1.className = 'ticker-track';

  for (let i = 0; i < 4; i++) {
    track1.appendChild(templateItem.cloneNode(true));
  }

  // Clone track1 for the seamless infinite loop
  const track2 = track1.cloneNode(true);
  track2.setAttribute('aria-hidden', 'true');

  container.appendChild(track1);
  container.appendChild(track2);
}

function initMobilePhoneAnimation() {
  const menuBtn = document.getElementById('mockMenuButton');
  const sidebarCloseBtn = document.getElementById('mockSidebarClose');
  const sidebarDrawer = document.getElementById('mockSidebarDrawer');

  const userBubble = document.getElementById('mockUserMessage');
  const thinkingBubble = document.getElementById('mockThinkingBubble');
  const assistantBubble = document.getElementById('mockAssistantMessage');
  const inputField = document.getElementById('mockInputField');
  const sendButton = document.getElementById('mockSendButton');
  const chatScroll = document.getElementById('mockChatScroll');

  const modeBtnRag = document.getElementById('modeBtnRag');
  const modeBtnChatbot = document.getElementById('modeBtnChatbot');
  const modeBtnImage = document.getElementById('modeBtnImage');
  const activeTitle = document.getElementById('mockActiveModeTitle');
  const docBanner = document.getElementById('mockDocBanner');
  const viewRag = chatScroll;
  const viewChatbot = document.getElementById('viewChatbot');
  const viewImage = document.getElementById('viewImage');

  const chatbotMessagesList = document.getElementById('chatbotMessagesList');
  const imageMessagesList = document.getElementById('imageMessagesList');

  if (!userBubble || !thinkingBubble || !assistantBubble) return;

  let currentMode = 'rag';
  let isAnimating = false;
  let chatbotHasGreeted = false;

  const modeConfigs = {
    chatbot: {
      btn: modeBtnChatbot,
      title: 'Chatbot',
      placeholder: 'Type a message...',
      showDoc: false,
      view: viewChatbot
    },
    image: {
      btn: modeBtnImage,
      title: 'Image Creation',
      placeholder: 'Describe an image...',
      showDoc: false,
      view: viewImage
    },
    rag: {
      btn: modeBtnRag,
      title: 'Documents RAG',
      placeholder: 'Parse the document...',
      showDoc: true,
      view: viewRag
    }
  };

  function adjustInputHeight() {
    if (!inputField) return;
    inputField.style.height = 'auto';
    const minHeight = 18;
    const maxHeight = 76;
    const targetHeight = Math.min(Math.max(inputField.scrollHeight, minHeight), maxHeight);
    inputField.style.height = targetHeight + 'px';
    inputField.style.overflowY = inputField.scrollHeight > maxHeight ? 'auto' : 'hidden';
  }

  function selectMode(mode) {
    currentMode = mode;
    const config = modeConfigs[mode] || modeConfigs.rag;

    [modeBtnRag, modeBtnChatbot, modeBtnImage].forEach(btn => {
      if (btn) btn.classList.toggle('active', btn === config.btn);
    });

    if (activeTitle) activeTitle.textContent = config.title;
    if (docBanner) docBanner.style.display = config.showDoc ? 'flex' : 'none';

    if (inputField) {
      inputField.placeholder = config.placeholder;
      inputField.value = '';
      adjustInputHeight();
      if (document.activeElement === inputField) {
        inputField.blur();
      }
    }

    [viewRag, viewChatbot, viewImage].forEach(view => {
      if (view) view.style.display = view === config.view ? 'flex' : 'none';
    });

    if (sidebarDrawer) sidebarDrawer.classList.remove('open');
  }

  [
    [modeBtnChatbot, 'chatbot'],
    [modeBtnImage, 'image'],
    [modeBtnRag, 'rag']
  ].forEach(([btn, mode]) => {
    if (btn) btn.addEventListener('click', () => selectMode(mode));
  });

  if (menuBtn) {
    menuBtn.addEventListener('click', () => {
      if (inputField && document.activeElement === inputField) {
        inputField.blur();
      }
      if (sidebarDrawer) sidebarDrawer.classList.toggle('open');
    });
  }

  if (sidebarCloseBtn) {
    sidebarCloseBtn.addEventListener('click', () => {
      if (sidebarDrawer) sidebarDrawer.classList.remove('open');
    });
  }

  function escapeHTML(str) {
    return str.replace(/[&<>'"]/g, tag => ({
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      "'": '&#39;',
      '"': '&quot;'
    }[tag] || tag));
  }

  function getAndClearInput() {
    if (!inputField) return '';
    const text = inputField.value.trim();
    inputField.value = '';
    adjustInputHeight();
    return text;
  }

  function appendBubble(container, type, contentHTML, extraClass = '') {
    if (!container) return;
    const msg = document.createElement('div');
    msg.className = `compose-message-bubble ${type} animate-in ${extraClass}`.trim();
    msg.innerHTML = `
      <span class="bubble-meta">09:41 AM</span>
      <div class="bubble-content ${type}-content">${contentHTML}</div>
    `;
    container.appendChild(msg);
    const scrollContainer = container.closest('.compose-chat-area') || container;
    scrollContainer.scrollTop = scrollContainer.scrollHeight;
  }

  function handleChatbotSend() {
    const text = getAndClearInput();
    if (!text) return;

    appendBubble(chatbotMessagesList, 'user', escapeHTML(text));

    const clean = text.toLowerCase().replace(/[^a-z]/g, '');
    let replyText = 'Download the app';
    if (!chatbotHasGreeted && (clean === 'hello' || clean === 'hi')) {
      replyText = 'Hello, how can I help you?';
      chatbotHasGreeted = true;
    }

    setTimeout(() => {
      appendBubble(chatbotMessagesList, 'assistant', escapeHTML(replyText));
    }, 350);
  }

  function handleImageSend() {
    const text = getAndClearInput();
    if (!text) return;

    appendBubble(imageMessagesList, 'user', escapeHTML(text));

    setTimeout(() => {
      appendBubble(imageMessagesList, 'assistant', 'Download the app');
    }, 400);
  }

  function handleRagSend() {
    if (isAnimating) return;
    const text = getAndClearInput();
    if (!text) return;

    appendBubble(chatScroll, 'user', escapeHTML(text), 'dynamic-extra-msg');

    setTimeout(() => {
      appendBubble(chatScroll, 'assistant', 'Download the app', 'dynamic-extra-msg');
    }, 350);
  }

  const modeHandlers = {
    chatbot: handleChatbotSend,
    image: handleImageSend,
    rag: handleRagSend
  };

  function handleSend() {
    const handler = modeHandlers[currentMode];
    if (handler) handler();
  }

  if (inputField) {
    inputField.addEventListener('keydown', (e) => {
      if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        handleSend();
      }
    });
    inputField.addEventListener('input', adjustInputHeight);
    adjustInputHeight();
  }

  const textFieldContainer = document.querySelector('.compose-text-field');
  if (textFieldContainer && inputField) {
    textFieldContainer.addEventListener('click', (e) => {
      if (e.target !== inputField) {
        inputField.focus();
      }
    });
  }

  if (sendButton) {
    sendButton.addEventListener('click', handleSend);
  }

  async function runMobileAnimation() {
    if (isAnimating) return;
    isAnimating = true;

    selectMode('rag');
    if (chatScroll) chatScroll.querySelectorAll('.dynamic-extra-msg').forEach(el => el.remove());
    userBubble.style.display = 'none';
    thinkingBubble.style.display = 'none';
    assistantBubble.style.display = 'none';
    if (inputField) {
      inputField.value = '';
      adjustInputHeight();
    }
    if (sidebarDrawer) sidebarDrawer.classList.remove('open');

    await sleep(600);

    const bubbleEl = document.getElementById('mockUserBubbleText');
    const queryText = (bubbleEl && bubbleEl.textContent.trim()) || 'Explain TCP connection and 3 way handshake according to the document i sent.';

    if (inputField) {
      inputField.value = '';
      adjustInputHeight();
      for (let i = 0; i < queryText.length; i++) {
        inputField.value += queryText[i];
        adjustInputHeight();
        inputField.scrollTop = inputField.scrollHeight;
        inputField.scrollLeft = inputField.scrollWidth;
        await sleep(20);
      }
    }

    await sleep(300);

    if (sendButton) {
      sendButton.style.transform = 'scale(0.88)';
      sendButton.style.backgroundColor = '#E5E5EA';
      await sleep(180);
      sendButton.style.transform = 'scale(1)';
      sendButton.style.backgroundColor = '#FFFFFF';
    }

    if (inputField) {
      inputField.value = '';
      adjustInputHeight();
    }

    userBubble.style.display = 'flex';
    userBubble.classList.add('animate-in');
    if (chatScroll) chatScroll.scrollTop = chatScroll.scrollHeight;

    await sleep(450);

    thinkingBubble.style.display = 'flex';
    if (chatScroll) chatScroll.scrollTop = chatScroll.scrollHeight;

    await sleep(1100);

    thinkingBubble.style.display = 'none';
    assistantBubble.style.display = 'flex';
    assistantBubble.classList.add('animate-in');
    if (chatScroll) chatScroll.scrollTop = chatScroll.scrollHeight;

    isAnimating = false;
  }

  let hasAutoPlayed = false;
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting && !hasAutoPlayed) {
        hasAutoPlayed = true;
        setTimeout(runMobileAnimation, 400);
      }
    });
  }, { threshold: 0.35 });

  const phoneSection = document.querySelector('.phone-stage');
  if (phoneSection) {
    observer.observe(phoneSection);
  }
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function initPhoneCursorTracking() {
  const chassis = document.querySelector('.phone-device-chassis');
  if (!chassis) return;

  const inputField = document.getElementById('mockInputField');

  let targetRotateX = 0;
  let targetRotateY = 0;
  let currentRotateX = 0;
  let currentRotateY = 0;
  let rafId = null;
  let isVisible = false;
  let isInteracting = false; // Set to true when user clicks inside phone

  const maxRotateX = 14; // vertical tilt degrees
  const maxRotateY = 16; // horizontal turn degrees
  const lerpFactor = 0.08; // smooth spring easing

  function update() {
    currentRotateX += (targetRotateX - currentRotateX) * lerpFactor;
    currentRotateY += (targetRotateY - currentRotateY) * lerpFactor;

    // Apply 3D perspective rotation so phone faces the cursor
    chassis.style.transform = `perspective(1200px) rotateX(${currentRotateX.toFixed(2)}deg) rotateY(${currentRotateY.toFixed(2)}deg)`;

    // Physically accurate shadow shifting opposite to the tilt direction
    const shadowX = (-currentRotateY * 1.6).toFixed(1);
    const shadowY = (35 - currentRotateX * 1.4).toFixed(1);
    chassis.style.boxShadow = `${shadowX}px ${shadowY}px 70px -15px rgba(0, 0, 0, 0.95), 0 0 40px rgba(255, 255, 255, 0.04)`;

    const delta = Math.abs(targetRotateX - currentRotateX) + Math.abs(targetRotateY - currentRotateY);
    if (delta > 0.01) {
      rafId = requestAnimationFrame(update);
    } else {
      rafId = null;
    }
  }

  function startAnimation() {
    if (!rafId) {
      rafId = requestAnimationFrame(update);
    }
  }

  function handlePointerMove(clientX, clientY) {
    if (!isVisible) return;

    // When user clicked inside the phone to interact, lock tilt completely to 0 (flat & steady)
    if (isInteracting) {
      targetRotateX = 0;
      targetRotateY = 0;
      startAnimation();
      return;
    }

    const rect = chassis.getBoundingClientRect();
    const isCursorInsideChassis = (
      clientX >= rect.left &&
      clientX <= rect.right &&
      clientY >= rect.top &&
      clientY <= rect.bottom
    );

    // Mitigate animation strength when cursor hovers directly over the phone
    const dampingFactor = isCursorInsideChassis ? 0.35 : 1.0;

    const centerX = rect.left + rect.width / 2;
    const centerY = rect.top + rect.height / 2;

    const deltaX = clientX - centerX;
    const deltaY = clientY - centerY;

    const radiusX = Math.max(window.innerWidth * 0.45, 380);
    const radiusY = Math.max(window.innerHeight * 0.45, 380);

    const normX = Math.max(-1, Math.min(1, deltaX / radiusX));
    const normY = Math.max(-1, Math.min(1, deltaY / radiusY));

    // When cursor is right (normX > 0) -> face turns right (rotateY > 0)
    // When cursor is above (normY < 0) -> face tilts up (rotateX > 0)
    targetRotateY = normX * maxRotateY * dampingFactor;
    targetRotateX = -normY * maxRotateX * dampingFactor;

    startAnimation();
  }

  function handleReset() {
    targetRotateX = 0;
    targetRotateY = 0;
    startAnimation();
  }

  // When user clicks inside phone, stop / stabilize tilt animation
  chassis.addEventListener('pointerdown', () => {
    isInteracting = true;
    handleReset();
  });

  // When user clicks outside phone, resume normal cursor tracking
  document.addEventListener('pointerdown', (e) => {
    if (!chassis.contains(e.target)) {
      isInteracting = false;
    }
  });

  // When cursor leaves phone chassis, release interaction lock (unless still typing in input)
  chassis.addEventListener('mouseleave', () => {
    const isInputFocused = inputField && document.activeElement === inputField;
    if (!isInputFocused) {
      isInteracting = false;
    }
  });

  if (inputField) {
    inputField.addEventListener('blur', () => {
      isInteracting = false;
    });
  }

  window.addEventListener('mousemove', (e) => {
    handlePointerMove(e.clientX, e.clientY);
  }, { passive: true });

  document.addEventListener('mouseleave', handleReset);

  // Touch support for touch devices
  window.addEventListener('touchmove', (e) => {
    if (e.touches && e.touches[0]) {
      handlePointerMove(e.touches[0].clientX, e.touches[0].clientY);
    }
  }, { passive: true });

  window.addEventListener('touchend', handleReset);

  // Only track when mobile chassis is visible in the viewport
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      isVisible = entry.isIntersecting;
      if (!isVisible) {
        handleReset();
      }
    });
  }, { threshold: 0.1 });

  observer.observe(chassis);
}