// OLAGU On-Device Intelligence — Mobile Interface Animation & Interaction

document.addEventListener('DOMContentLoaded', () => {
  initMobilePhoneAnimation();
});

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
  const viewRag = document.getElementById('mockChatScroll');
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
      view: viewChatbot,
      focus: true
    },
    image: {
      btn: modeBtnImage,
      title: 'Image Creation',
      placeholder: 'Describe an image...',
      showDoc: false,
      view: viewImage,
      focus: true
    },
    rag: {
      btn: modeBtnRag,
      title: 'Documents RAG',
      placeholder: 'Prase the document..',
      showDoc: true,
      view: viewRag,
      focus: false
    }
  };

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
      if (config.focus) inputField.focus();
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
    container.scrollTop = container.scrollHeight;
  }

  function handleChatbotSend() {
    const text = getAndClearInput();
    if (!text) return;

    appendBubble(chatbotMessagesList, 'user', escapeHTML(text));

    const clean = text.toLowerCase().replace(/[^a-z]/g, '');
    let replyText = 'Download an app';
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
      appendBubble(imageMessagesList, 'assistant', 'Download an app');
    }, 400);
  }

  function handleRagSend() {
    if (isAnimating) return;
    const text = getAndClearInput();
    if (!text) return;

    appendBubble(chatScroll, 'user', escapeHTML(text), 'dynamic-extra-msg');

    setTimeout(() => {
      appendBubble(chatScroll, 'assistant', 'Download an app', 'dynamic-extra-msg');
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
      if (e.key === 'Enter') handleSend();
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
    if (inputField) inputField.value = '';
    if (sidebarDrawer) sidebarDrawer.classList.remove('open');

    await sleep(600);

    const bubbleEl = document.getElementById('mockUserBubbleText');
    const queryText = (bubbleEl && bubbleEl.textContent.trim()) || 'Explain TCP connection and 3 way handshake according to the document i sent.';

    if (inputField) {
      inputField.value = '';
      for (let i = 0; i < queryText.length; i++) {
        inputField.value += queryText[i];
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

    if (inputField) inputField.value = '';

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

  const phoneSection = document.querySelector('.mobile-showcase-wrapper');
  if (phoneSection) {
    observer.observe(phoneSection);
  }
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
