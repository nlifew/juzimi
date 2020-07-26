

#ifndef _HTML_H
#define _HTML_H

#include <vector>
#include <map>
#include <memory>
#include "gumbo/gumbo.h"



class HtmlNode
{
private:
    ::std::map<GumboNode*, HtmlNode*> mNodeCache;
	HtmlNode* wrapNode(GumboNode *p);

	template <typename T>
	HtmlNode* filter(::std::vector<HtmlNode*> *v, T const& func);

protected:
	GumboNode *mNode;
	HtmlNode(GumboNode *node = nullptr) : 
		mNode(node) {
	}
    
public:
	static HtmlNode* emptyNode() { static HtmlNode emptyNode; return &emptyNode; }

	bool isEmpty() { return mNode == nullptr; }

	virtual ~HtmlNode();

	HtmlNode(const HtmlNode&) = delete;
	HtmlNode& operator=(const HtmlNode&) = delete; 


	const char* attr(const char *key);

	int attrs(::std::map<const char*, const char*>& map);

	const char* text();

	const char* tag();

	HtmlNode* parent();

	int children(::std::vector<HtmlNode*>& v);

	int childCount();

	HtmlNode* childAt(int index);

	HtmlNode* getNodeByTag(::std::vector<HtmlNode*> *v, const char *tag);

	HtmlNode* getNodeByAttr(::std::vector<HtmlNode*> *v, const char *name, const char *value);

	HtmlNode* getNodeByAttrStarting(::std::vector<HtmlNode*> *v, const char *name, const char *valuePrefix);

	HtmlNode* getNodeByAttrEnding(::std::vector<HtmlNode*> *v, const char *name, const char *valuePrefix);

	HtmlNode* getNodeByAttrContaining(::std::vector<HtmlNode*> *v, const char *name, const char *value);

	HtmlNode* getNodeByClass(::std::vector<HtmlNode*> *v, const char *clazz) { return getNodeByAttr(v, "class", clazz); }

	HtmlNode* getNodeById(const char *id) { return getNodeByAttr(nullptr, "id", id); }
};



class HtmlTree : public HtmlNode
{
public:
	GumboOutput *mTree;

public:
	HtmlTree(const char *buff);
	~HtmlTree();
};
#endif