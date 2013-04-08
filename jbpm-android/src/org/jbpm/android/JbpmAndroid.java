package org.jbpm.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.impl.EnvironmentFactory;
import org.drools.process.core.Work;
import org.drools.process.core.datatype.impl.type.StringDataType;
import org.drools.process.core.impl.WorkImpl;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.process.ProcessBaseFactoryService;
import org.jbpm.process.ProcessPackage;
import org.jbpm.process.core.context.variable.Variable;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.impl.ConnectionImpl;
import org.jbpm.workflow.core.impl.NodeImpl;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.StartNode;
import org.jbpm.workflow.core.node.WorkItemNode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class JbpmAndroid extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		RuleFlowProcess process = new RuleFlowProcess();
		process.setId("org.jbpm.android.hello");
		process.setPackageName("org.jbpm.android");
		VariableScope variableScope = (VariableScope) process.getContexts(VariableScope.VARIABLE_SCOPE).get(0);
		List<Variable> variables = new ArrayList<Variable>();
		Variable url = new Variable();
		url.setName("url");
		url.setType(new StringDataType());
		variables.add(url);
		variableScope.setVariables(variables);
		StartNode startNode = new StartNode();
		startNode.setId(1);
		process.addNode(startNode);
		WorkItemNode workItemNode1 = new WorkItemNode();
		Work work1 = new WorkImpl();
		work1.setName("RequestTextInput");
		workItemNode1.setWork(work1);
		workItemNode1.addOutMapping("Text", "url");
		workItemNode1.setId(2);
		process.addNode(workItemNode1);
		WorkItemNode workItemNode2 = new WorkItemNode();
		Work work2 = new WorkImpl();
		work2.setName("ShowWebPage");
		workItemNode2.setWork(work2);
		workItemNode2.addInMapping("URL", "url");
		workItemNode2.setId(3);
		process.addNode(workItemNode2);
		EndNode endNode = new EndNode();
		endNode.setId(4);
		process.addNode(endNode);
		new ConnectionImpl(startNode, NodeImpl.CONNECTION_DEFAULT_TYPE, workItemNode1, NodeImpl.CONNECTION_DEFAULT_TYPE);
		new ConnectionImpl(workItemNode1, NodeImpl.CONNECTION_DEFAULT_TYPE, workItemNode2, NodeImpl.CONNECTION_DEFAULT_TYPE);
		new ConnectionImpl(workItemNode2, NodeImpl.CONNECTION_DEFAULT_TYPE, endNode, NodeImpl.CONNECTION_DEFAULT_TYPE);
		System.out.println("Created process");
		KnowledgeBaseFactory.setKnowledgeBaseServiceFactory(new ProcessBaseFactoryService());
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		List<KnowledgePackage> packages = new ArrayList<KnowledgePackage>();
		ProcessPackage p = new ProcessPackage("org.jbpm.android.hello");
		p.addProcess(process);
		packages.add(p);
		kbase.addKnowledgePackages(packages);
		
		Properties properties = new Properties();
		properties.put("drools.processInstanceManagerFactory", "org.jbpm.process.instance.impl.DefaultProcessInstanceManagerFactory");
		properties.put("drools.processSignalManagerFactory", "org.jbpm.process.instance.event.DefaultSignalManagerFactory");
		KnowledgeSessionConfiguration config = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(properties);
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(config, EnvironmentFactory.newEnvironment());
		ksession.getWorkItemManager().registerWorkItemHandler("RequestTextInput", new WorkItemHandler() {
			public void abortWorkItem(WorkItem workItem, WorkItemManager m) {
			}
			public void executeWorkItem(final WorkItem workItem, final WorkItemManager m) {
				setContentView(R.layout.main);
				Button button = (Button) findViewById(R.id.ok);
				button.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						EditText text = (EditText) findViewById(R.id.entry);
						final String message = text.getText().toString();
						Map<String, Object> results = new HashMap<String, Object>();
						results.put("Text", "http://community.jboss.org/search.jspa?rankBy=date&q=" + message);
						m.completeWorkItem(workItem.getId(), results);
					}
				});
			}
		});
		ksession.getWorkItemManager().registerWorkItemHandler("ShowWebPage", new WorkItemHandler() {
			public void abortWorkItem(WorkItem workItem, WorkItemManager m) {
			}
			public void executeWorkItem(final WorkItem workItem, final WorkItemManager m) {
				String url = (String) workItem.getParameter("URL");
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				m.completeWorkItem(workItem.getId(), null);
			}
		});
		ksession.startProcess("org.jbpm.android.hello");
    }
}