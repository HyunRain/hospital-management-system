window.onload = function() {
  window.ui = SwaggerUIBundle({
    urls: [
      { url: "/api-docs/auth", name: "auth" },
      { url: "/api-docs/patient", name: "patient" }
    ],
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    layout: "StandaloneLayout"
  });
};