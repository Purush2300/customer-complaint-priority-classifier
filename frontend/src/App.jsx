
import { useEffect, useState } from "react";
import "./App.css";

const API_URL = "http://localhost:9090/api/complaints";

function App() {
  const [activePage, setActivePage] = useState("Dashboard");
  const [complaints, setComplaints] = useState([]);
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");
  const [lastAnalysis, setLastAnalysis] = useState(null);

  async function loadComplaints() {
    setLoading(true);
    setError("");

    try {
      const response = await fetch(API_URL);

      if (!response.ok) {
        throw new Error(`Unable to load complaints (${response.status})`);
      }

      const data = await response.json();
      setComplaints(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(
        err instanceof TypeError
          ? "Cannot connect to Spring Boot. Check that the backend is running on port 9090 and CORS is configured."
          : err.message
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadComplaints();
  }, []);

  async function submitComplaint(useAI) {
    if (!description.trim()) {
      setError("Please enter a complaint description.");
      return;
    }

    setSaving(true);
    setError("");
    setNotice("");
    setLastAnalysis(null);

    try {
      const response = await fetch(
        useAI ? `${API_URL}/analyze` : API_URL,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ description: description.trim() }),
        }
      );

      const result = await response.json().catch(() => null);

      if (!response.ok) {
        throw new Error(
          result?.message ||
            result?.error ||
            `Failed to submit complaint (${response.status})`
        );
      }

      setDescription("");
      setNotice(
        useAI
          ? "Complaint analyzed and saved successfully."
          : "Complaint created successfully."
      );

      if (useAI) setLastAnalysis(result);

      await loadComplaints();
    } catch (err) {
      setError(
        err instanceof TypeError
          ? "Cannot reach the backend. Check the server and CORS settings."
          : err.message
      );
    } finally {
      setSaving(false);
    }
  }

  async function deleteComplaint(id) {
    if (!window.confirm(`Delete complaint #${id}?`)) return;

    setError("");
    setNotice("");

    try {
      const response = await fetch(`${API_URL}/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error(`Delete failed (${response.status})`);
      }

      setComplaints((current) =>
        current.filter((complaint) => complaint.id !== id)
      );
      setNotice(`Complaint #${id} deleted.`);
    } catch (err) {
      setError(err.message);
    }
  }

  const openCount = complaints.filter(
    (item) => item.status?.toUpperCase() === "OPEN"
  ).length;

  const highCount = complaints.filter((item) =>
    ["HIGH", "URGENT"].includes(item.priority?.toUpperCase())
  ).length;

  const urgentCount = complaints.filter(
    (item) => item.priority?.toUpperCase() === "URGENT"
  ).length;

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-icon">C</div>
          <div>
            <h2>ComplainIQ</h2>
            <p>AI Support Desk</p>
          </div>
        </div>

        <p className="menu-label">WORKSPACE</p>

        <button
          className={`nav-item ${activePage === "Dashboard" ? "active" : ""}`}
          onClick={() => setActivePage("Dashboard")}
        >
          <span>▦</span> Dashboard
        </button>

        <button
          className={`nav-item ${activePage === "Complaints" ? "active" : ""}`}
          onClick={() => setActivePage("Complaints")}
        >
          <span>☷</span> Complaints
        </button>

        <div className="sidebar-bottom">
          <div className="ai-status">
            <span className="status-dot"></span>
            <div>
              <strong>AI Assistant</strong>
              <p>Gemini integration</p>
            </div>
          </div>
        </div>
      </aside>

      <main className="main-content">
        <header className="topbar">
          <div>
            <p className="breadcrumb">Workspace / {activePage}</p>
            <h1>{activePage}</h1>
          </div>
          <div className="profile">
            <div className="avatar">P</div>
            <div>
              <strong>Administrator</strong>
              <p>Support team</p>
            </div>
          </div>
        </header>

        <section className="content">
          {error && <div className="message error">{error}</div>}
          {notice && <div className="message success">{notice}</div>}

          {activePage === "Dashboard" ? (
            <>
              <div className="welcome">
                <div>
                  <p className="eyebrow">COMPLAINT OVERVIEW</p>
                  <h2>Customer support dashboard</h2>
                  <p>Track customer issues and prioritize complaints using AI.</p>
                </div>
                <div className="welcome-icon">✦</div>
              </div>

              <div className="stats-grid">
                <StatCard label="Total complaints" value={complaints.length} icon="▤" />
                <StatCard label="Open complaints" value={openCount} icon="◷" />
                <StatCard label="High priority +" value={highCount} icon="⚑" />
                <StatCard label="Urgent priority" value={urgentCount} icon="!" />
              </div>

              <div className="panel">
                <div className="panel-heading">
                  <div>
                    <h3>Recent complaints</h3>
                    <p>Latest customer issues from your database</p>
                  </div>
                  <button
                    className="primary-button"
                    onClick={() => setActivePage("Complaints")}
                  >
                    Manage complaints →
                  </button>
                </div>

                <ComplaintList
                  complaints={complaints.slice(0, 5)}
                  loading={loading}
                  onDelete={deleteComplaint}
                />
              </div>
            </>
          ) : (
            <>
              <div className="welcome">
                <div>
                  <p className="eyebrow">SUPPORT WORKSPACE</p>
                  <h2>Manage customer complaints</h2>
                  <p>Create complaints or let AI classify their priority.</p>
                </div>
                <div className="welcome-icon">✦</div>
              </div>

              <div className="panel form-panel">
                <h3>New complaint</h3>
                <p>Describe the customer's issue clearly.</p>

                <textarea
                  value={description}
                  onChange={(event) => setDescription(event.target.value)}
                  placeholder="Example: I was charged twice for my order and have not received a refund..."
                  maxLength={5000}
                  rows={5}
                />

                <div className="form-footer">
                  <span>{description.length}/5000 characters</span>
                  <div className="form-actions">
                    <button
                      className="secondary-button"
                      disabled={saving}
                      onClick={() => submitComplaint(false)}
                    >
                      Save complaint
                    </button>
                    <button
                      className="primary-button"
                      disabled={saving}
                      onClick={() => submitComplaint(true)}
                    >
                      {saving ? "Processing..." : "✦ Analyze with AI"}
                    </button>
                  </div>
                </div>

                {lastAnalysis && (
                  <div className="analysis-result">
                    <h3>AI analysis result</h3>
                    <p>
                      <strong>Priority: </strong>
                      <Priority value={lastAnalysis.priority} />
                    </p>
                    <p><strong>Summary:</strong> {lastAnalysis.summary || "—"}</p>
                    <p><strong>Reason:</strong> {lastAnalysis.reason || "—"}</p>
                    <p>
                      <strong>Suggested action:</strong>{" "}
                      {lastAnalysis.suggestedAction || "—"}
                    </p>
                  </div>
                )}
              </div>

              <div className="panel">
                <div className="panel-heading">
                  <div>
                    <h3>All complaints</h3>
                    <p>Stored in your MySQL database through Spring Boot</p>
                  </div>
                  <button className="secondary-button" onClick={loadComplaints}>
                    Refresh
                  </button>
                </div>

                <ComplaintList
                  complaints={complaints}
                  loading={loading}
                  onDelete={deleteComplaint}
                />
              </div>
            </>
          )}
        </section>
      </main>
    </div>
  );
}

function StatCard({ label, value, icon }) {
  return (
    <div className="stat-card">
      <div className="stat-top">
        <span>{label}</span>
        <span className="stat-icon">{icon}</span>
      </div>
      <h2>{value}</h2>
      <p>Live database count</p>
    </div>
  );
}

function Priority({ value }) {
  const priority = value?.toUpperCase() || "UNKNOWN";

  return (
    <span className={`priority priority-${priority.toLowerCase()}`}>
      {priority}
    </span>
  );
}

function ComplaintList({ complaints, loading, onDelete }) {
  if (loading) return <p className="list-message">Loading complaints...</p>;

  if (!complaints.length) {
    return <p className="list-message">No complaints found yet.</p>;
  }

  return (
    <div className="table-wrapper">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Description</th>
            <th>Priority</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {complaints.map((item) => (
            <tr key={item.id}>
              <td>#{item.id}</td>
              <td className="description-cell">
                {item.description?.length > 75
                  ? `${item.description.slice(0, 75)}...`
                  : item.description}
              </td>
              <td><Priority value={item.priority} /></td>
              <td><span className="status-label">{item.status || "OPEN"}</span></td>
              <td>
                <button
                  className="delete-button"
                  onClick={() => onDelete(item.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;
